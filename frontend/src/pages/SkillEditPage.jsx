import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { skillService } from '../services/skillService';
import Input from '../components/common/Input';
import TextArea from '../components/common/TextArea';
import Button from '../components/common/Button';
import Alert from '../components/common/Alert';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ImagePicker from '../components/common/ImagePicker';

export default function SkillEditPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [skill, setSkill] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    titre: '',
    description: '',
    images: [],
    disponibilites: '',
    commentaireEchange: '',
    actif: true,
  });

  useEffect(() => {
    loadSkill();
  }, [id]);

  const loadSkill = async () => {
    try {
      const res = await skillService.getById(id);
      setSkill(res.data);
      let imgs = [];
      if (Array.isArray(res.data.images)) imgs = res.data.images;
      else if (typeof res.data.images === 'string' && res.data.images.trim() !== '') {
        try {
          const parsed = JSON.parse(res.data.images);
          if (Array.isArray(parsed)) imgs = parsed;
          else imgs = res.data.images.split(',').map(s => s.trim()).filter(s => s);
        } catch (e) {
          imgs = res.data.images.split(',').map(s => s.trim()).filter(s => s);
        }
      }

      setForm({
        titre: res.data.titre || '',
        description: res.data.description || '',
        images: imgs,
        disponibilites: res.data.disponibilites || '',
        commentaireEchange: res.data.commentaireEchange || '',
        actif: res.data.actif !== undefined ? res.data.actif : true,
      });
    } catch (err) {
      console.error('Error loading skill:', err);
      setError('Impossible de charger la compétence');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, type, value, checked } = e.target;
    setForm({ ...form, [name]: type === 'checkbox' ? checked : value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      const dataToSubmit = {
        ...form,
        images: form.images && form.images.length > 0 ? JSON.stringify(form.images) : null,
      };
      await skillService.update(id, dataToSubmit);
      navigate(`/skills/${id}`);
    } catch (err) {
      console.error('Error updating skill:', err);
      setError(err.response?.data?.message || 'Erreur lors de la mise à jour');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-4">Modifier la compétence</h1>
      {error && <Alert type="error" message={error} onClose={() => setError('')} />}

      <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow">
        <Input label="Titre" name="titre" value={form.titre} onChange={handleChange} required />

        <TextArea label="Description" name="description" value={form.description} onChange={handleChange} required />

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">Images (optionnel)</label>
          <ImagePicker images={form.images} onChange={(imgs) => setForm({ ...form, images: imgs })} />
        </div>

        <TextArea
          label="Commentaire pour l'échange (optionnel)"
          name="commentaireEchange"
          value={form.commentaireEchange}
          onChange={handleChange}
          placeholder="Indiquez ce que vous recherchez en échange"
          maxLength={500}
        />

        <TextArea label="Disponibilités" name="disponibilites" value={form.disponibilites} onChange={handleChange} required />

        <div className="flex items-center gap-2">
          <input
            id="actif"
            name="actif"
            type="checkbox"
            checked={!!form.actif}
            onChange={handleChange}
            className="h-4 w-4"
          />
          <label htmlFor="actif" className="text-sm text-gray-700">Actif</label>
        </div>

        <div className="flex gap-3">
          <Button type="submit" className="flex-1" disabled={submitting}>
            {submitting ? 'Mise à jour...' : 'Mettre à jour'}
          </Button>
          <Button type="button" variant="secondary" onClick={() => navigate(`/skills/${id}`)}>
            Annuler
          </Button>
        </div>
      </form>
    </div>
  );
}
