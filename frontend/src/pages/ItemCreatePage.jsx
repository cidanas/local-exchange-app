import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { itemService } from '../services/itemService';
import { CATEGORIES } from '../utils/constants';
import Input from '../components/common/Input';
import TextArea from '../components/common/TextArea';
import Select from '../components/common/Select';
import Button from '../components/common/Button';
import Alert from '../components/common/Alert';
import ImagePicker from '../components/common/ImagePicker';

export default function ItemCreatePage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ titre: '', description: '', categorie: '', images: [], commentaireEchange: '', disponibilite: true });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, type, value, checked } = e.target;
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const dataToSubmit = {
        ...form,
        images: form.images && form.images.length > 0 ? JSON.stringify(form.images) : null,
      };
      const res = await itemService.create(dataToSubmit);
      // navigate to newly created item's detail page (backend should return created resource)
      const newId = res.data?.id;
      if (newId) navigate(`/items/${newId}`);
      else navigate('/items');
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-4">Créer une annonce</h1>
      {error && <Alert type="error" message={error} onClose={() => setError('')} />}

      <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow">
        <Input label="Titre" name="titre" value={form.titre} onChange={handleChange} required />

        <TextArea label="Description" name="description" value={form.description} onChange={handleChange} required />

        <Select
          label="Catégorie"
          name="categorie"
          options={CATEGORIES}
          value={form.categorie}
          onChange={handleChange}
          required
        />

        <TextArea
          label="Commentaire pour l'échange (optionnel)"
          name="commentaireEchange"
          value={form.commentaireEchange}
          onChange={handleChange}
          placeholder="Indiquez ce que vous recherchez en échange"
          maxLength={500}
        />

        <div className="flex items-center gap-2">
          <input
            id="disponibilite"
            name="disponibilite"
            type="checkbox"
            checked={!!form.disponibilite}
            onChange={handleChange}
            className="h-4 w-4"
          />
          <label htmlFor="disponibilite" className="text-sm text-gray-700">Disponible</label>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Images (optionnel)</label>
          <ImagePicker images={form.images} onChange={(imgs) => setForm({ ...form, images: imgs })} />
        </div>
        <div className="flex gap-3">
          <Button type="submit" className="flex-1" disabled={loading}>
            {loading ? 'Création...' : 'Créer'}
          </Button>
          <Button type="button" variant="secondary" onClick={() => navigate('/items')}>
            Annuler
          </Button>
        </div>
      </form>
    </div>
  );
}
