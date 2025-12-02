import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { notificationService } from '../services/notificationService';

const NotificationContext = createContext();

export function NotificationProvider({ children }) {
  const [unreadCount, setUnreadCount] = useState(0);

  const loadUnreadCount = useCallback(async () => {
    try {
      const res = await notificationService.getUnreadCount();
      setUnreadCount(res.data?.count || 0);
    } catch (error) {
      console.error('Error loading notification count:', error);
    }
  }, []);

  const decrementCount = useCallback(() => {
    setUnreadCount((prev) => Math.max(0, prev - 1));
  }, []);

  const decrementCountBy = useCallback((amount) => {
    setUnreadCount((prev) => Math.max(0, prev - amount));
  }, []);

  const resetCount = useCallback(() => {
    setUnreadCount(0);
  }, []);

  // Charger le compteur au montage et le rafraîchir tous les 10 secondes
  useEffect(() => {
    loadUnreadCount();
    const interval = setInterval(loadUnreadCount, 10000);
    return () => clearInterval(interval);
  }, [loadUnreadCount]);

  return (
    <NotificationContext.Provider
      value={{
        unreadCount,
        loadUnreadCount,
        decrementCount,
        decrementCountBy,
        resetCount,
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
}

export function useNotification() {
  return useContext(NotificationContext);
}
