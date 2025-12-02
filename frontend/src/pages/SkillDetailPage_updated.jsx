import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { MapPin, Calendar, MessageSquare, Edit, Trash2, Briefcase } from 'lucide-react';
import { skillService } from '../services/skillService';
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

export default function SkillDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [skill, setSkill] = useState(null);
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
    loadSkill();
  }, [id]);

  const loadSkill = async () => {
    try {
      const res = await skillService.getById(id);
      setSkill(res.data);
      // parse images field which may be JSON string or CSV or array
      let imgs = [];
      const raw = res.data.images;
      if (Array.isArray(raw)) imgs = raw;
      else if (typeof raw === 'string' && raw.trim() !== '') {
        try {
          const parsed = JSON.parse(raw);
          if (Array.isArray(parsed)) imgs = parsed;
          else imgs = raw.split(',').map(s => s.trim()).filter(s => s);
        } catch (e) {
          imgs = raw.split(',').map(s => s.trim()).filter(s => s);
        }
      }
      setImages(imgs);
    } catch (error) {
      console.error('Error loading skill:', error);
      setError('Compétence introuvable');
    } finally {
      setLoading(false);
    }
  };

  const handleExchangeSubmit = async (e) => {
    e.preventDefault();
    try {
      await exchangeService.create({
        ...exchangeData,
        skillListingId: parseInt(id),
      });
      setSuccess('Demande d\'échange envoyée avec succès !');
      setShowExchangeModal(false);
      setTimeout(() => navigate('/exchanges'), 2000);
    } catch (error) {
      setError(error.response?.data?.message || 'Erreur lors de l\'envoi de la demande');
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Êtes-vous sûr de vouloir supprimer cette compétence ?')) return;
    try {
      await skillService.delete(id);
      navigate('/skills');
    } catch (error) {
      setError('Erreur lors de la suppression');
    }
  };

  if (loading) return <LoadingSpinner />;
  if (!skill) return <div className="text-center py-12">Compétence introuvable</div>;

  const isOwner = user?.id === skill.owner?.id;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {error && <Alert type="error" message={error} onClose={() => setError('')} />}
      {success && <Alert type="success" message={success} />}

      <div className="grid md:grid-cols-2 gap-8">
        {/* Images/Illustration */}
        <div>
          {images && images.length > 0 ? (
            <div className="rounded-xl overflow-hidden">
              <img src={images[0]} alt="main" className="w-full h-96 object-cover rounded-lg" />
              {images.length > 1 && (
                <div className="mt-2 flex gap-2">
                  {images.map((src, idx) => (
                    <img key={idx} src={src} alt={`thumb-${idx}`} className="w-20 h-20 object-cover rounded cursor-pointer" />
                  ))}
                </div>
              )}
            </div>
          ) : (
            <div className="bg-gradient-to-br from-purple-100 to-pink-100 rounded-xl h-96 flex items-center justify-center">
              <Briefcase size={128} className="text-purple-400" />
            </div>
          )}
        </div>

        {/* Détails */}
        <div>
          <div className="flex items-start justify-between mb-4">
            <div>
              <h1 className="text-4xl font-bold">{skill.titre}</h1>
            </div>
            {isOwner && (
              <div className="flex gap-2">
                <Button variant="secondary" size="sm" onClick={() => navigate(`/skills/${id}/edit`)}>
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
            <span>{skill.owner?.localisation}</span>
          </div>

          <div className="mb-6">
            <h3 className="font-semibold text-lg mb-2">Description</h3>
            <p className="text-gray-700 whitespace-pre-wrap">{skill.description}</p>
          </div>

          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
            <h3 className="font-semibold mb-2 flex items-center gap-2">
              <Calendar size={18} />
              Disponibilités
            </h3>
            <p className="text-gray-700">{skill.disponibilites}</p>
          </div>

          {skill.commentaireEchange && (
            <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
              <h3 className="font-semibold mb-2">Échange souhaité</h3>
              <p className="text-gray-700">{skill.commentaireEchange}</p>
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
                  <Link to={`/profile/${skill.owner?.id}`} className="font-semibold text-lg hover:text-indigo-600">
                    {skill.owner?.nom}
                  </Link>
                  <Rating value={skill.owner?.averageRating || 0} />
                  <p className="text-sm text-gray-600 mt-1">
                    {skill.owner?.totalExchanges || 0} échange(s) réalisé(s)
                  </p>
                </div>
              </div>
            </div>
          </Card>

          {/* Boutons d'action */}
          {!isOwner && user && skill.actif && (
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

          {!skill.actif && (
            <div className="bg-gray-100 text-gray-600 py-3 px-4 rounded-lg text-center">
              Cette compétence n'est plus disponible
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
