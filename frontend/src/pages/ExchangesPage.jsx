import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, User, Package, Briefcase, Check, X, MessageSquare } from 'lucide-react';
import { exchangeService } from '../services/exchangeService';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import Badge from '../components/common/Badge';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Alert from '../components/common/Alert';
import { EXCHANGE_STATUS } from '../utils/constants';

export default function ExchangesPage() {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('received');
  const [receivedRequests, setReceivedRequests] = useState([]);
  const [sentRequests, setSentRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadExchanges();
  }, []);

  const loadExchanges = async () => {
    setLoading(true);
    try {
      const [receivedRes, sentRes] = await Promise.all([
        exchangeService.getReceived(),
        exchangeService.getSent(),
      ]);
      setReceivedRequests(receivedRes.data || []);
      setSentRequests(sentRes.data || []);
    } catch (error) {
      console.error('Error loading exchanges:', error);
      setError('Erreur lors du chargement des échanges');
    } finally {
      setLoading(false);
    }
  };

  const handleAccept = async (id) => {
    try {
      await exchangeService.accept(id);
      setSuccess('Demande acceptée !');
      loadExchanges();
    } catch (error) {
      setError('Erreur lors de l\'acceptation');
    }
  };

  const handleRefuse = async (id) => {
    if (!window.confirm('Êtes-vous sûr de vouloir refuser cette demande ?')) return;
    try {
      await exchangeService.refuse(id);
      setSuccess('Demande refusée');
      loadExchanges();
    } catch (error) {
      setError('Erreur lors du refus');
    }
  };

  const handleComplete = async (id) => {
    if (!window.confirm('Marquer cet échange comme terminé ?')) return;
    try {
      await exchangeService.complete(id);
      setSuccess('Échange marqué comme terminé ! Vous pouvez maintenant laisser un avis.');
      loadExchanges();
    } catch (error) {
      setError('Erreur lors de la finalisation');
    }
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      PENDING: 'warning',
      ACCEPTED: 'success',
      REFUSED: 'danger',
      COMPLETED: 'primary',
      CANCELLED: 'default',
    };
    return <Badge variant={statusMap[status] || 'default'}>{EXCHANGE_STATUS[status] || status}</Badge>;
  };

  const ExchangeCard = ({ exchange, isReceived }) => (
    <Card className="mb-4 hover:shadow-lg transition">
      <div className="p-6">
        <div className="flex items-start justify-between mb-4">
          <div className="flex items-center gap-4 flex-1">
            <div className="bg-indigo-100 p-3 rounded-full">
              <User className="text-indigo-600" size={24} />
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-1">
                <h3 className="font-semibold text-lg">
                  {isReceived ? exchange.beneficiaireNom : exchange.donateurNom}
                </h3>
                {getStatusBadge(exchange.statut)}
              </div>
              <div className="text-sm text-gray-600 mb-2">
                {exchange.itemTitre && (
                  <div className="flex items-center gap-1">
                    <Package size={14} />
                    <span>Objet : {exchange.itemTitre}</span>
                  </div>
                )}
                {exchange.skillTitre && (
                  <div className="flex items-center gap-1">
                    <Briefcase size={14} />
                    <span>Compétence : {exchange.skillTitre}</span>
                  </div>
                )}
              </div>
              <p className="text-gray-700 mb-2">
                <strong>Offre en retour :</strong> {exchange.offreEnRetour}
              </p>
              {exchange.messageInitial && (
                <p className="text-gray-600 text-sm italic">
                  "{exchange.messageInitial}"
                </p>
              )}
              <div className="flex items-center gap-2 mt-2 text-sm text-gray-500">
                <Calendar size={14} />
                <span>Date proposée : {new Date(exchange.dateEchange).toLocaleDateString('fr-FR')}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="flex gap-2 mt-4">
          {isReceived && exchange.statut === 'PENDING' && (
            <>
              <Button
                variant="success"
                size="sm"
                onClick={() => handleAccept(exchange.id)}
                className="flex items-center gap-1"
              >
                <Check size={16} />
                Accepter
              </Button>
              <Button
                variant="danger"
                size="sm"
                onClick={() => handleRefuse(exchange.id)}
                className="flex items-center gap-1"
              >
                <X size={16} />
                Refuser
              </Button>
            </>
          )}

          {exchange.statut === 'ACCEPTED' && (
            <>
              <Button
                size="sm"
                onClick={() => navigate(`/messages/${exchange.id}`)}
                className="flex items-center gap-1"
              >
                <MessageSquare size={16} />
                Messagerie
              </Button>
              <Button
                variant="success"
                size="sm"
                onClick={() => handleComplete(exchange.id)}
              >
                Marquer comme terminé
              </Button>
            </>
          )}

          {exchange.statut === 'COMPLETED' && (
            <Button
              variant="primary"
              size="sm"
              onClick={() => navigate(`/reviews/create?exchange=${exchange.id}`)}
            >
              Laisser un avis
            </Button>
          )}
        </div>
      </div>
    </Card>
  );

  if (loading) return <LoadingSpinner />;

  const requests = activeTab === 'received' ? receivedRequests : sentRequests;

  return (
    <div className="max-w-5xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Mes échanges</h1>

      {error && <Alert type="error" message={error} onClose={() => setError('')} />}
      {success && <Alert type="success" message={success} onClose={() => setSuccess('')} />}

      <Card>
        {/* Tabs */}
        <div className="border-b">
          <div className="flex">
            <button
              onClick={() => setActiveTab('received')}
              className={`px-6 py-4 font-semibold transition ${
                activeTab === 'received'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              Demandes reçues ({receivedRequests.length})
            </button>
            <button
              onClick={() => setActiveTab('sent')}
              className={`px-6 py-4 font-semibold transition ${
                activeTab === 'sent'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-600 hover:text-gray-900'
              }`}
            >
              Demandes envoyées ({sentRequests.length})
            </button>
          </div>
        </div>

        {/* Liste */}
        <div className="p-6">
          {requests.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <p className="text-lg">Aucune demande d'échange</p>
              <p className="text-sm mt-2">
                {activeTab === 'received'
                  ? 'Les demandes que vous recevrez apparaîtront ici'
                  : 'Commencez par faire une demande d\'échange'}
              </p>
            </div>
          ) : (
            requests.map((exchange) => (
              <ExchangeCard
                key={exchange.id}
                exchange={exchange}
                isReceived={activeTab === 'received'}
              />
            ))
          )}
        </div>
      </Card>
    </div>
  );
}