import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Search, Plus, Briefcase } from 'lucide-react';
import { skillService } from '../services/skillService';
import { useAuth } from '../context/AuthContext';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Rating from '../components/common/Rating';

export default function SkillsPage() {
  const { user } = useAuth();
  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    loadSkills();
  }, [search, page]);

  const loadSkills = async () => {
    setLoading(true);
    try {
      const params = { page, size: 12 };
      if (search) params.search = search;

      const res = await skillService.getAll(params);
      setSkills(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } catch (error) {
      console.error('Error loading skills:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    loadSkills();
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Compétences disponibles</h1>
        {user && (
          <Link to="/skills/create">
            <Button className="flex items-center gap-2">
              <Plus size={20} />
              Proposer une compétence
            </Button>
          </Link>
        )}
      </div>

      {/* Recherche */}
      <div className="bg-white p-4 rounded-lg shadow mb-6">
        <form onSubmit={handleSearch} className="flex gap-4">
          <div className="flex-1">
            <Input
              placeholder="Rechercher une compétence..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <Button type="submit" className="flex items-center gap-2">
            <Search size={20} />
            Rechercher
          </Button>
        </form>
      </div>

      {/* Liste des compétences */}
      {loading ? (
        <LoadingSpinner />
      ) : skills.length === 0 ? (
        <div className="text-center py-12 text-gray-500">
          <p className="text-xl">Aucune compétence trouvée</p>
          <p className="mt-2">Essayez de modifier votre recherche</p>
        </div>
      ) : (
        <>
          <div className="grid md:grid-cols-3 gap-6">
            {skills.map((skill) => {
              let imageUrl = null;
              if (skill.images) {
                try {
                  let imagePaths = [];
                  if (skill.images.startsWith('[')) {
                    imagePaths = JSON.parse(skill.images);
                  } else if (skill.images.includes(',')) {
                    imagePaths = skill.images.split(',').map(s => s.trim());
                  } else {
                    imagePaths = [skill.images];
                  }
                  if (imagePaths.length > 0) imageUrl = imagePaths[0];
                } catch (e) {
                  imageUrl = skill.images;
                }
              }

              return (
              <Link key={skill.id} to={`/skills/${skill.id}`}>
                <Card hover>
                  <div className="bg-gradient-to-br from-purple-100 to-pink-100 h-48 flex items-center justify-center overflow-hidden">
                    {imageUrl ? (
                      <img src={imageUrl} alt={skill.titre} className="w-full h-full object-cover" />
                    ) : (
                      <div className="flex items-center justify-center w-full h-full">
                        <Briefcase size={48} className="text-purple-600" />
                      </div>
                    )}
                  </div>
                  <div className="p-6">
                    <h3 className="text-xl font-bold mb-2">{skill.titre}</h3>
                    <p className="text-gray-600 text-sm line-clamp-2 mb-3">
                      {skill.description}
                    </p>
                    <p className="text-xs text-gray-500 mb-3">
                      Disponibilités : {skill.disponibilites}
                    </p>
                    <div className="flex items-center justify-between">
                      <span className="text-sm text-gray-600">{skill.ownerNom}</span>
                      <Rating value={skill.ownerRating || 0} size={14} />
                    </div>
                  </div>
                </Card>
              </Link>
              );
            })}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center gap-2 mt-8">
              <Button
                variant="secondary"
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
              >
                Précédent
              </Button>
              <span className="px-4 py-2 text-gray-600">
                Page {page + 1} sur {totalPages}
              </span>
              <Button
                variant="secondary"
                disabled={page >= totalPages - 1}
                onClick={() => setPage(page + 1)}
              >
                Suivant
              </Button>
            </div>
          )}
        </>
      )}
    </div>
  );
}