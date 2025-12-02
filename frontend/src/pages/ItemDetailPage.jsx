import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { MapPin, Calendar, MessageSquare, Edit, Trash2 } from 'lucide-react';
import { itemService } from '../services/itemService';
import { exchangeService } from '../services/exchangeService';
import { useAuth } from '../context/AuthContext';
import Button from '../components/common/Button';
import Modal from '../components/common/Modal';
import Input from '../components/common/Input';
import TextArea from '../components/common/TextArea';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Alert from '../components/common/Alert';
import Rating from '../components/common/Rating';
import Card from '../components/common/Card';

export default function ItemDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [item, setItem] = useState(null);
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showExchangeModal, setShowExchangeModal] = useState(false);
  const [exchangeData, setExchangeData] = useState({
    offreEnRetour: '',
    dateEchange: '',
    messageInitial: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadItem();
  }, [id]);

  const loadItem = async () => {
    try {
      const res = await itemService.getById(id);
      setItem(res.data);
      console.log('Item data:', res.data);
      console.log('Raw images field:', res.data.images);
      
      // parse images field which may be JSON string or CSV or array
      let imgs = [];
      const raw = res.data.images;
      if (Array.isArray(raw)) imgs = raw;
      else if (typeof raw === 'string' && raw.trim() !== '') {
        try {
          const parsed = JSON.parse(raw);
          console.log('Parsed JSON images:', parsed);
          if (Array.isArray(parsed)) imgs = parsed;
          else imgs = raw.split(',').map(s => s.trim()).filter(s => s);
        } catch (e) {
          console.log('Failed to parse as JSON, trying CSV:', e);
          imgs = raw.split(',').map(s => s.trim()).filter(s => s);
        }
      }
      console.log('Final images array:', imgs);
      setImages(imgs);
    } catch (error) {
      console.error('Error loading item:', error);
      setError('Objet introuvable');
    } finally {
      setLoading(false);
    }
  };

  const handleExchangeSubmit = async (e) => {
    e.preventDefault();
    try {
      await exchangeService.create({
        ...exchangeData,
        itemListingId: parseInt(id),
      });
      setSuccess('Demande d\'échange envoyée avec succès !');
      setShowExchangeModal(false);
      setTimeout(() => navigate('/exchanges'), 2000);
    } catch (error) {
      setError(error.response?.data?.message || 'Erreur lors de l\'envoi de la demande');
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Êtes-vous sûr de vouloir supprimer cette annonce ?')) return;
    try {
      await itemService.delete(id);
      navigate('/items');
    } catch (error) {
      setError('Erreur lors de la suppression');
    }
  };

  if (loading) return <LoadingSpinner />;
  if (!item) return <div className="text-center py-12">Objet introuvable</div>;

  const isOwner = user?.id === item.owner?.id;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {error && <Alert type="error" message={error} onClose={() => setError('')} />}
      {success && <Alert type="success" message={success} />}

      <div className="grid md:grid-cols-2 gap-8">
        {/* Images */}
        <div>
          {images && images.length > 0 ? (
            <div className="rounded-xl overflow-hidden">
              {console.log('Rendering images:', images)}
              <img 
                src={images[0]} 
                alt="main" 
                className="w-full h-96 object-cover rounded-lg" 
                onError={(e) => {
                  console.error('Image load error for:', images[0]);
                  e.target.style.border = '2px solid red';
                }}
              />
              {images.length > 1 && (
                <div className="mt-2 flex gap-2">
                  {images.map((src, idx) => (
                    <img 
                      key={idx} 
                      src={src} 
                      alt={`thumb-${idx}`} 
                      className="w-20 h-20 object-cover rounded cursor-pointer" 
                      onError={(e) => {
                        console.error('Thumbnail load error for:', src);
                        e.target.style.border = '2px solid red';
                      }}
                    />
                  ))}
                </div>
              )}
            </div>
          ) : (
            <div className="bg-gradient-to-br from-indigo-100 to-purple-100 rounded-xl h-96 flex items-center justify-center">
              {console.log('No images, images =', images)}
              <span className="text-9xl">📦</span>
            </div>
          )}
        </div>

        {/* Détails */}
        <div>
          <div className="flex items-start justify-between mb-4">
            <div>
              <span className="bg-indigo-100 text-indigo-700 px-3 py-1 rounded text-sm font-semibold">
                {item.categorie}
              </span>
              <h1 className="text-4xl font-bold mt-2">{item.titre}</h1>
            </div>
            {isOwner && (
              <div className="flex gap-2">
                <Button variant="secondary" size="sm" onClick={() => navigate(`/items/${id}/edit`)}>
                  <Edit size={16} />
                </Button>
                <Button variant="danger" size="sm" onClick={handleDelete}>
                  <Trash2 size={16} />
                </Button>
              </div>
            )}
          </div>

          <div className="flex items-center gap-2 text-gray-600 mb-4">
            <MapPin size={18} />
            <span>{item.owner?.localisation}</span>
          </div>

          <div className="mb-6">
            <h3 className="font-semibold text-lg mb-2">Description</h3>
            <p className="text-gray-700 whitespace-pre-wrap">{item.description}</p>
          </div>

          {item.commentaireEchange && (
            <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
              <h3 className="font-semibold mb-2">Échange souhaité</h3>
              <p className="text-gray-700">{item.commentaireEchange}</p>
            </div>
          )}

          {/* Propriétaire */}
          <Card className="mb-6">
            <div className="p-4">
              <h3 className="font-semibold mb-3">Proposé par</h3>
              <div className="flex items-center gap-4">
                <div className="bg-gray-300 w-16 h-16 rounded-full flex items-center justify-center text-2xl">
                  👤
                </div>
                <div className="flex-1">
                  <Link to={`/profile/${item.owner?.id}`} className="font-semibold text-lg hover:text-indigo-600">
                    {item.owner?.nom}
                  </Link>
                  <Rating value={item.owner?.averageRating || 0} />
                  <p className="text-sm text-gray-600 mt-1">
                    {item.owner?.totalExchanges || 0} échange(s) réalisé(s)
                  </p>
                </div>
              </div>
            </div>
          </Card>

          {/* Boutons d'action */}
          {!isOwner && user && item.disponibilite && (
            <Button
              className="w-full flex items-center justify-center gap-2"
              onClick={() => setShowExchangeModal(true)}
            >
              <MessageSquare size={20} />
              Faire une demande d'échange
            </Button>
          )}

          {!user && (
            <Link to="/login">
              <Button className="w-full">
                Connectez-vous pour faire une demande
              </Button>
            </Link>
          )}

          {!item.disponibilite && (
            <div className="bg-gray-100 text-gray-600 py-3 px-4 rounded-lg text-center">
              Cet objet n'est plus disponible
            </div>
          )}
        </div>
      </div>

      {/* Modal demande d'échange */}
      <Modal
        isOpen={showExchangeModal}
        onClose={() => setShowExchangeModal(false)}
        title="Faire une demande d'échange"
      >
        <form onSubmit={handleExchangeSubmit}>
          <TextArea
            label="Que proposez-vous en échange ?"
            name="offreEnRetour"
            value={exchangeData.offreEnRetour}
            onChange={(e) => setExchangeData({ ...exchangeData, offreEnRetour: e.target.value })}
            required
            placeholder="Décrivez ce que vous proposez en échange..."
            maxLength={500}
          />

          <Input
            label="Date proposée pour l'échange"
            type="date"
            name="dateEchange"
            value={exchangeData.dateEchange}
            onChange={(e) => setExchangeData({ ...exchangeData, dateEchange: e.target.value })}
            required
            // Backend validation requires a strictly future date (@Future),
            // so set the min to tomorrow to avoid submitting today's date.
            min={new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString().split('T')[0]}
          />

          <TextArea
            label="Message (optionnel)"
            name="messageInitial"
            value={exchangeData.messageInitial}
            onChange={(e) => setExchangeData({ ...exchangeData, messageInitial: e.target.value })}
            placeholder="Ajoutez un message pour le propriétaire..."
            maxLength={1000}
          />

          <div className="flex gap-3">
            <Button type="button" variant="secondary" onClick={() => setShowExchangeModal(false)} className="flex-1">
              Annuler
            </Button>
            <Button type="submit" className="flex-1">
              Envoyer la demande
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}