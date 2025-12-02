import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Search, Plus } from 'lucide-react';
import { itemService } from '../services/itemService';
import { useAuth } from '../context/AuthContext';
import { CATEGORIES } from '../utils/constants';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Input from '../components/common/Input';
import Select from '../components/common/Select';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Rating from '../components/common/Rating';

export default function ItemsPage() {
  const { user } = useAuth();
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [categorie, setCategorie] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    loadItems();
  }, [search, categorie, page]);

  const loadItems = async () => {
    setLoading(true);
    try {
      const params = { page, size: 12 };
      if (search) params.search = search;
      if (categorie) params.categorie = categorie;

      const res = await itemService.getAll(params);
      setItems(res.data.content || []);
      setTotalPages(res.data.totalPages || 0);
    } catch (error) {
      console.error('Error loading items:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setPage(0);
    loadItems();
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Objets disponibles</h1>
        {user && (
          <Link to="/items/create">
            <Button className="flex items-center gap-2">
              <Plus size={20} />
              Créer une annonce
            </Button>
          </Link>
        )}
      </div>

      {/* Filtres et recherche */}
      <div className="bg-white p-4 rounded-lg shadow mb-6">
        <form onSubmit={handleSearch} className="flex gap-4 flex-wrap">
          <div className="flex-1 min-w-[200px]">
            <Input
              placeholder="Rechercher un objet..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>
          <div className="w-48">
            <Select
              options={CATEGORIES}
              value={categorie}
              onChange={(e) => setCategorie(e.target.value)}
              placeholder="Toutes catégories"
            />
          </div>
          <Button type="submit" className="flex items-center gap-2">
            <Search size={20} />
            Rechercher
          </Button>
        </form>
      </div>

      {/* Liste des items */}
      {loading ? (
        <LoadingSpinner />
      ) : items.length === 0 ? (
        <div className="text-center py-12 text-gray-500">
          <p className="text-xl">Aucun objet trouvé</p>
          <p className="mt-2">Essayez de modifier vos critères de recherche</p>
        </div>
      ) : (
        <>
          <div className="grid md:grid-cols-3 gap-6">
            {items.map((item) => {
              let imageUrl = null;
              if (item.images) {
                try {
                  let imagePaths = [];
                  if (item.images.startsWith('[')) {
                    imagePaths = JSON.parse(item.images);
                  } else if (item.images.includes(',')) {
                    imagePaths = item.images.split(',').map(s => s.trim());
                  } else {
                    imagePaths = [item.images];
                  }
                  if (imagePaths.length > 0) imageUrl = imagePaths[0];
                } catch (e) {
                  imageUrl = item.images;
                }
              }

              return (
              <Link key={item.id} to={`/items/${item.id}`}>
                <Card hover>
                  <div className="bg-gradient-to-br from-indigo-100 to-purple-100 h-48 flex items-center justify-center overflow-hidden">
                    {imageUrl ? (
                      <img src={imageUrl} alt={item.titre} className="w-full h-full object-cover" />
                    ) : (
                      <span className="text-6xl">📦</span>
                    )}
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