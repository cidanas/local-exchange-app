import { useState, useEffect } from 'react';
import { Edit, Package, Briefcase, Star } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { authService } from '../services/authService';
import { reviewService } from '../services/reviewService';
import { itemService } from '../services/itemService';
import { skillService } from '../services/skillService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import TextArea from '../components/common/TextArea';
import Modal from '../components/common/Modal';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Rating from '../components/common/Rating';
import Alert from '../components/common/Alert';

export default function ProfilePage() {
  const { user, updateUser, loadUser } = useAuth();
  const [reviews, setReviews] = useState([]);
  const [myItems, setMyItems] = useState([]);
  const [mySkills, setMySkills] = useState([]);
  const [activeTab, setActiveTab] = useState('items');
  const [showEditModal, setShowEditModal] = useState(false);
  const [editData, setEditData] = useState({});
  const [loading, setLoading] = useState(true);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (user) {
      loadProfileData();
    }
  }, [user]);

  const loadProfileData = async () => {
    setLoading(true);
    try {
      const [itemsRes, skillsRes, reviewsRes] = await Promise.all([
        itemService.getMyItems(),
        skillService.getMySkills(),
        reviewService.getUserReviews(user.id),
      ]);
      setMyItems(itemsRes.data || []);
      setMySkills(skillsRes.data || []);
      setReviews(reviewsRes.data || []);
      setEditData({
        nom: user.nom,
        localisation: user.localisation,
        bio: user.bio || '',
        phoneNumber: user.phoneNumber || '',
      });
    } catch (error) {
      console.error('Error loading profile:', error);
      setError('Erreur lors du chargement du profil');
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    try {
      await authService.updateProfile(editData);
      setSuccess('Profil mis à jour avec succès !');
      setShowEditModal(false);
      await loadUser();
      loadProfileData();
    } catch (error) {
      setError('Erreur lors de la mise à jour');
    }
  };

  if (loading) return <LoadingSpinner />;
  if (!user) return <div className="text-center py-12">Profil introuvable</div>;

  return (
    <div className="max-w-5xl mx-auto px-4 py-8">
      {error && <Alert type="error" message={error} onClose={() => setError('')} />}
      {success && <Alert type="success" message={success} onClose={() => setSuccess('')} />}

      {/* En-tête du profil */}
      <Card className="mb-6">
        <div className="bg-gradient-to-r from-indigo-500 to-purple-600 h-32 rounded-t-xl" />
        <div className="px-6 pb-6">
          <div className="flex items-end justify-between -mt-16 mb-6">
            <div className="bg-white p-2 rounded-full">
              <div className="bg-gray-300 w-32 h-32 rounded-full flex items-center justify-center text-5xl">
                {user.photo ? (
                  <img
                    src={user.photo}
                    alt={user.nom}
                    className="w-full h-full rounded-full object-cover"
                  />
                ) : (
                  '👤'
                )}
              </div>
            </div>
            <Button
              variant="secondary"
              onClick={() => setShowEditModal(true)}
              className="flex items-center gap-2"
            >
              <Edit size={16} />
              Modifier profil
            </Button>
          </div>

          <h1 className="text-3xl font-bold">{user.nom}</h1>
          <p className="text-gray-600 mt-1">
            {user.localisation} • Membre depuis{' '}
            {new Date(user.createdAt).getFullYear()}
          </p>
          {user.bio && (
            <p className="text-gray-700 mt-4 whitespace-pre-wrap">{user.bio}</p>
          )}

          {/* Statistiques */}
          <div className="grid grid-cols-3 gap-4 mt-6">
            <div className="bg-indigo-50 p-4 rounded-lg text-center">
              <div className="text-3xl font-bold text-indigo-600">
                {user.totalExchanges || 0}
              </div>
              <div className="text-sm text-gray-600 mt-1">Échanges réalisés</div>
            </div>
            <div className="bg-purple-50 p-4 rounded-lg text-center">
              <div className="text-3xl font-bold text-purple-600 flex items-center justify-center gap-2">
                {user.averageRating?.toFixed(1) || '0.0'}
                <Star size={24} className="text-yellow-400 fill-yellow-400" />
              </div>
              <div className="text-sm text-gray-600 mt-1">Note moyenne</div>
            </div>
            <div className="bg-green-50 p-4 rounded-lg text-center">
              <div className="text-3xl font-bold text-green-600">
                {myItems.length + mySkills.length}
              </div>
              <div className="text-sm text-gray-600 mt-1">Annonces actives</div>
            </div>
          </div>
        </div>
      </Card>

      {/* Onglets */}
      <Card>
        <div className="border-b">
          <div className="flex">
            <button
              onClick={() => setActiveTab('items')}
              className={`px-6 py-4 font-semibold transition flex items-center gap-2 ${
                activeTab === 'items'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <Package size={20} />
              Objets ({myItems.length})
            </button>
            <button
              onClick={() => setActiveTab('skills')}
              className={`px-6 py-4 font-semibold transition flex items-center gap-2 ${
                activeTab === 'skills'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <Briefcase size={20} />
              Compétences ({mySkills.length})
            </button>
            <button
              onClick={() => setActiveTab('reviews')}
              className={`px-6 py-4 font-semibold transition flex items-center gap-2 ${
                activeTab === 'reviews'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              <Star size={20} />
              Avis ({reviews.length})
            </button>
          </div>
        </div>

        <div className="p-6">
          {/* Liste des objets */}
          {activeTab === 'items' && (
            <div className="grid md:grid-cols-2 gap-4">
              {myItems.length === 0 ? (
                <p className="text-gray-500 col-span-2 text-center py-12">
                  Aucun objet publié
                </p>
              ) : (
                myItems.map((item) => (
                  <Card key={item.id} hover>
                    <div className="p-4">
                      <h3 className="font-bold text-lg">{item.titre}</h3>
                      <p className="text-gray-600 text-sm mt-1 line-clamp-2">
                        {item.description}
                      </p>
                      <span className="inline-block mt-2 text-xs bg-indigo-100 text-indigo-700 px-2 py-1 rounded">
                        {item.categorie}
                      </span>
                    </div>
                  </Card>
                ))
              )}
            </div>
          )}

          {/* Liste des compétences */}
          {activeTab === 'skills' && (
            <div className="grid md:grid-cols-2 gap-4">
              {mySkills.length === 0 ? (
                <p className="text-gray-500 col-span-2 text-center py-12">
                  Aucune compétence publiée
                </p>
              ) : (
                mySkills.map((skill) => (
                  <Card key={skill.id} hover>
                    <div className="p-4">
                      <h3 className="font-bold text-lg">{skill.titre}</h3>
                      <p className="text-gray-600 text-sm mt-1 line-clamp-2">
                        {skill.description}
                      </p>
                      <p className="text-xs text-gray-500 mt-2">
                        Disponibilités : {skill.disponibilites}
                      </p>
                    </div>
                  </Card>
                ))
              )}
            </div>
          )}

          {/* Liste des avis */}
          {activeTab === 'reviews' && (
            <div className="space-y-4">
              {reviews.length === 0 ? (
                <p className="text-gray-500 text-center py-12">Aucun avis reçu</p>
              ) : (
                reviews.map((review) => (
                  <Card key={review.id}>
                    <div className="p-4">
                      <div className="flex items-center justify-between mb-2">
                        <div className="flex items-center gap-2">
                          <span className="font-semibold">{review.reviewerNom}</span>
                          <Rating value={review.notation} showValue={false} />
                        </div>
                        <span className="text-sm text-gray-500">
                          {new Date(review.createdAt).toLocaleDateString('fr-FR')}
                        </span>
                      </div>
                      <p className="text-gray-700">{review.commentaire}</p>
                    </div>
                  </Card>
                ))
              )}
            </div>
          )}
        </div>
      </Card>

      {/* Modal d'édition */}
      <Modal
        isOpen={showEditModal}
        onClose={() => setShowEditModal(false)}
        title="Modifier mon profil"
      >
        <form onSubmit={handleUpdateProfile}>
          <Input
            label="Nom"
            name="nom"
            value={editData.nom}
            onChange={(e) => setEditData({ ...editData, nom: e.target.value })}
            required
          />
          <Input
            label="Localisation"
            name="localisation"
            value={editData.localisation}
            onChange={(e) => setEditData({ ...editData, localisation: e.target.value })}
            required
          />
          <TextArea
            label="Biographie"
            name="bio"
            value={editData.bio}
            onChange={(e) => setEditData({ ...editData, bio: e.target.value })}
            maxLength={500}
            rows={4}
          />
          <Input
            label="Numéro de téléphone"
            name="phoneNumber"
            value={editData.phoneNumber}
            onChange={(e) => setEditData({ ...editData, phoneNumber: e.target.value })}
          />
          <div className="flex gap-3">
            <Button type="button" variant="secondary" onClick={() => setShowEditModal(false)} className="flex-1">
              Annuler
            </Button>
            <Button type="submit" className="flex-1">
              Enregistrer
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}