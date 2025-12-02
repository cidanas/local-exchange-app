import { Link } from 'react-router-dom';
import { Home, Package, Briefcase, MessageSquare, Bell, User, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const { unreadCount, loadUnreadCount } = useNotification();

  return (
    <nav className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white shadow-lg sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <div className="bg-white text-indigo-600 rounded-lg p-2">
              <Home size={24} />
            </div>
            <span className="text-2xl font-bold">LocalExchange</span>
          </Link>

          <div className="flex items-center gap-6">
            <Link to="/" className="hover:text-indigo-200 transition flex items-center gap-2">
              <Home size={20} />
              <span className="hidden md:inline">Accueil</span>
            </Link>
            <Link to="/items" className="hover:text-indigo-200 transition flex items-center gap-2">
              <Package size={20} />
              <span className="hidden md:inline">Objets</span>
            </Link>
            <Link to="/skills" className="hover:text-indigo-200 transition flex items-center gap-2">
              <Briefcase size={20} />
              <span className="hidden md:inline">Compétences</span>
            </Link>

            {user ? (
              <>
                <Link to="/exchanges" className="hover:text-indigo-200 transition flex items-center gap-2">
                  <MessageSquare size={20} />
                  <span className="hidden md:inline">Échanges</span>
                </Link>
                <Link to="/notifications" className="hover:text-indigo-200 transition relative">
                  <Bell size={20} />
                  {unreadCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
                      {unreadCount}
                    </span>
                  )}
                </Link>
                <Link to="/profile" className="hover:text-indigo-200 transition">
                  <User size={20} />
                </Link>
                <button onClick={logout} className="hover:text-indigo-200 transition" title="Déconnexion">
                  <LogOut size={20} />
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="hover:text-indigo-200 transition">
                  Connexion
                </Link>
                <Link to="/register" className="bg-white text-indigo-600 px-4 py-2 rounded-lg hover:bg-indigo-50 transition font-semibold">
                  Inscription
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}