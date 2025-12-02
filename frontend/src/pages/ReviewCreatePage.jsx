import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { reviewService } from '../services/reviewService';
import { exchangeService } from '../services/exchangeService';
import Input from '../components/common/Input';
import TextArea from '../components/common/TextArea';
import Button from '../components/common/Button';
import Alert from '../components/common/Alert';
import Rating from '../components/common/Rating';
import LoadingSpinner from '../components/common/LoadingSpinner';

export default function ReviewCreatePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const exchangeId = searchParams.get('exchange');
  
  const [exchange, setExchange] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    notation: 5,
    commentaire: '',
    exchangeRequestId: exchangeId ? parseInt(exchangeId) : null,
  });

  useEffect(() => {
    if (exchangeId) {
      loadExchange();
    }
  }, [exchangeId]);

  const loadExchange = async () => {
    try {
      const res = await exchangeService.getById(exchangeId);
      setExchange(res.data);
      setForm((prev) => ({ ...prev, exchangeRequestId: parseInt(exchangeId) }));
    } catch (err) {
      console.error('Error loading exchange:', err);
      setError('Impossible de charger les détails de l\'échange');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: name === 'notation' ? parseInt(value) : value });
  };

  const handleRatingChange = (rating) => {
    setForm({ ...form, notation: rating });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    try {
      await reviewService.create(form);
      navigate('/exchanges');
    } catch (err) {
      console.error('Error creating review:', err);
      const errorMessage = err.response?.data?.message || err.message || 'Erreur lors de la création de l\'avis';
      setError(errorMessage);
      setSubmitting(false);
    }
  };

  if (loading) return <LoadingSpinner />;

  if (!exchangeId) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-8">
        <Alert type="error" message="ID d'échange manquant" />
      </div>
    );
  }

  const revieweeName = exchange?.beneficiaireName || 'l\'utilisateur';

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-2">Laisser un avis</h1>
      <p className="text-gray-600 mb-6">Partagez votre expérience avec {revieweeName}</p>

      {error && <Alert type="error" message={error} onClose={() => setError('')} />}

      <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Note <span className="text-red-500">*</span>
          </label>
          <div className="flex items-center gap-2">
            <Rating value={form.notation} onChange={handleRatingChange} editable={true} />
            <span className="text-lg font-semibold">{form.notation}/5</span>
          </div>
        </div>

        <TextArea
          label="Commentaire (optionnel)"
          name="commentaire"
          value={form.commentaire}
          onChange={handleChange}
          placeholder="Partagez votre expérience (ponctualité, état du bien, courtoisie, etc.)"
          maxLength={1000}
          rows={6}
        />

        <div className="flex gap-3">
          <Button type="submit" className="flex-1" disabled={submitting}>
            {submitting ? 'Envoi...' : 'Soumettre l\'avis'}
          </Button>
          <Button type="button" variant="secondary" onClick={() => navigate('/exchanges')}>
            Annuler
          </Button>
        </div>
      </form>
    </div>
  );
}
