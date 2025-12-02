import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Search, Package, MessageSquare, Star } from 'lucide-react';
import { itemService } from '../services/itemService';
import { skillService } from '../services/skillService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Rating from '../components/common/Rating';

export default function HomePage() {
  const [recentItems, setRecentItems] = useState([]);
  const [recentSkills, setRecentSkills] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadRecentData();
  }, []);

  const loadRecentData = async () => {
    try {
      const [itemsRes, skillsRes] = await Promise.all([
        itemService.getAll({ page: 0, size: 6 }),
        skillService.getAll({ page: 0, size: 6 }),
      ]);
      setRecentItems(itemsRes.data.content || []);
      setRecentSkills(skillsRes.data.content || []);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-indigo-500 to-purple-600 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h1 className="text-5xl font-bold mb-4">Partagez, Échangez, Connectez</h1>
          <p className="text-xl mb-8">
            La plateforme d'échange local de biens et compétences entre voisins
          </p>
          <div className="flex gap-4 justify-center flex-wrap">
            <Link to="/items">
              <Button size="lg" className="bg-white text-indigo-600 hover:bg-indigo-50">
                Explorer les objets
              </Button>
            </Link>
            <Link to="/skills">
              <Button size="lg" variant="secondary">
                Découvrir les compétences
              </Button>
            </Link>
          </div>
        </div>
      </div>

      {/* Comment ça marche */}
      <div className="max-w-7xl mx-auto px-4 py-16">
        <h2 className="text-3xl font-bold text-center mb-12">Comment ça marche ?</h2>
        <div className="grid md:grid-cols-3 gap-8">
          <div className="text-center p-6 bg-white rounded-xl shadow-md">
            <div className="bg-indigo-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Search size={32} className="text-indigo-600" />
            </div>
            <h3 className="text-xl font-bold mb-2">1. Recherchez</h3>
            <p className="text-gray-600">
              Trouvez des objets ou compétences près de chez vous
            </p>
          </div>

          <div className="text-center p-6 bg-white rounded-xl shadow-md">
            <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <MessageSquare size={32} className="text-purple-600" />
            </div>
            <h3 className="text-xl font-bold mb-2">2. Contactez</h3>
            <p className="text-gray-600">
              Proposez un échange et discutez des détails
            </p>
          </div>

          <div className="text-center p-6 bg-white rounded-xl shadow-md">
            <div className="bg-green-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
              <Star size={32} className="text-green-600" />
            </div>
            <h3 className="text-xl font-bold mb-2">3. Évaluez</h3>
            <p className="text-gray-600">
              Laissez un avis après chaque échange réussi
            </p>
          </div>
        </div>
      </div>

      {/* Derniers objets */}
      <div className="bg-gray-50 py-16">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold">Derniers objets disponibles</h2>
            <Link to="/items">
              <Button variant="secondary">Voir tout</Button>
            </Link>
          </div>

          {loading ? (
            <LoadingSpinner />
          ) : (
            <div className="grid md:grid-cols-3 gap-6">
              {recentItems.map((item) => (
                <Link key={item.id} to={`/items/${item.id}`}>
                  <Card hover>
                    <div className="bg-gradient-to-br from-indigo-100 to-purple-100 h-48 flex items-center justify-center">
                      <Package size={64} className="text-indigo-400" />
                    </div>
                    <div className="p-4">
                      <span className="text-xs bg-indigo-100 text-indigo-700 px-2 py-1 rounded">
                        {item.categorie}
                      </span>
                      <h3 className="text-lg font-bold mt-2">{item.titre}</h3>
                      <p className="text-gray-600 text-sm mt-1 line-clamp-2">
                        {item.description}
                      </p>
                      <div className="flex items-center justify-between mt-3">
                        <span className="text-sm text-gray-600">{item.ownerNom}</span>
                        <Rating value={item.ownerRating || 0} size={14} />
                      </div>
                    </div>
                  </Card>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Dernières compétences */}
      <div className="py-16">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold">Compétences proposées</h2>
            <Link to="/skills">
              <Button variant="secondary">Voir tout</Button>
            </Link>
          </div>

          {loading ? (
            <LoadingSpinner />
          ) : (
            <div className="grid md:grid-cols-3 gap-6">
              {recentSkills.map((skill) => (
                <Link key={skill.id} to={`/skills/${skill.id}`}>
                  <Card hover>
                    <div className="p-6">
                      <div className="bg-purple-100 w-12 h-12 rounded-lg flex items-center justify-center mb-4">
                        <Star size={24} className="text-purple-600" />
                      </div>
                      <h3 className="text-xl font-bold mb-2">{skill.titre}</h3>
                      <p className="text-gray-600 text-sm line-clamp-2 mb-4">
                        {skill.description}
                      </p>
                      <div className="flex items-center justify-between">
                        <span className="text-sm text-gray-600">{skill.ownerNom}</span>
                        <Rating value={skill.ownerRating || 0} size={14} />
                      </div>
                    </div>
                  </Card>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}