import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { notificationService } from '../services/notificationService';
import { useNotification } from '../context/NotificationContext';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Button from '../components/common/Button';
import Card from '../components/common/Card';
import { format } from 'date-fns';

export default function NotificationsPage() {
  const navigate = useNavigate();
  const { decrementCount, decrementCountBy } = useNotification();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadNotifications();
  }, []);

  const loadNotifications = async () => {
    setLoading(true);
    try {
      const res = await notificationService.getAll();
      setNotifications(res.data || []);
    } catch (err) {
      console.error('Error loading notifications', err);
      setError('Impossible de charger les notifications');
    } finally {
      setLoading(false);
    }
  };

  const handleMarkRead = async (id) => {
    try {
      await notificationService.markAsRead(id);
      setNotifications((prev) => prev.map(n => n.id === id ? { ...n, read: true } : n));
      decrementCount();
    } catch (err) {
      console.error('Error marking notification read', err);
      setError('Erreur lors de la mise à jour');
    }
  };

  const handleMarkAll = async () => {
    try {
      const unreadNotifications = notifications.filter(n => !n.read).length;
      await notificationService.markAllAsRead();
      setNotifications((prev) => prev.map(n => ({ ...n, read: true })));
      decrementCountBy(unreadNotifications);
    } catch (err) {
      console.error('Error marking all read', err);
      setError('Erreur lors de la mise à jour');
    }
  };

  const getNavigationLink = (notification) => {
    if (notification.exchangeId) {
      return `/messages/${notification.exchangeId}`;
    }
    if (notification.itemListingId) {
      return `/items/${notification.itemListingId}`;
    }
    if (notification.skillListingId) {
      return `/skills/${notification.skillListingId}`;
    }
    return null;
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Notifications</h1>
        <div className="flex items-center gap-2">
          <Button variant="secondary" onClick={loadNotifications}>Rafraîchir</Button>
          <Button onClick={handleMarkAll}>Marquer tout lu</Button>
        </div>
      </div>

      {error && <div className="text-red-500 mb-4">{error}</div>}

      {notifications.length === 0 ? (
        <div className="text-center py-12 text-gray-500">Aucune notification</div>
      ) : (
        <div className="space-y-4">
          {notifications.map((n) => {
            const navLink = getNavigationLink(n);
            return (
              <Card key={n.id} className={`${n.read ? '' : 'border-l-4 border-indigo-500'}`}>
                <div className="p-4 flex justify-between items-start">
                  <div className="flex-1">
                    <div className="text-sm text-gray-600">{n.type}</div>
                    <div className="font-medium mt-1">{n.title || n.message}</div>
                    {n.message && <div className="text-gray-700 mt-2">{n.message}</div>}
                    <div className="text-xs text-gray-500 mt-2">{n.createdAt ? format(new Date(n.createdAt), 'dd/MM/yyyy HH:mm') : ''}</div>
                  </div>
                  <div className="flex flex-col items-end gap-2 ml-4">
                    {navLink && (
                      <Button size="sm" onClick={() => {
                        if (!n.read) handleMarkRead(n.id);
                        navigate(navLink);
                      }}>
                        Consulter
                      </Button>
                    )}
                    {!n.read && !navLink && (
                      <Button size="sm" onClick={() => handleMarkRead(n.id)}>Marquer lu</Button>
                    )}
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      )}
    </div>
  );
}
