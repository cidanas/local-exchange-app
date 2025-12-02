import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { NotificationProvider } from './context/NotificationContext';
import Navbar from './components/layout/Navbar';
import Footer from './components/layout/Footer';
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ItemsPage from './pages/ItemsPage';
import ItemDetailPage from './pages/ItemDetailPage';
import ItemCreatePage from './pages/ItemCreatePage';
import ItemEditPage from './pages/ItemEditPage';
import SkillsPage from './pages/SkillsPage';
import SkillDetailPage from './pages/SkillDetailPage';
import SkillCreatePage from './pages/SkillCreatePage';
import SkillEditPage from './pages/SkillEditPage';
import ProfilePage from './pages/ProfilePage';
import ExchangesPage from './pages/ExchangesPage';
import MessagesPage from './pages/MessagesPage';
import LoadingSpinner from './components/common/LoadingSpinner';
import NotificationsPage from './pages/NotificationsPage';
import ReviewCreatePage from './pages/ReviewCreatePage';

const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth();
  
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    );
  }
  
  return user ? children : <Navigate to="/login" />;
};

function AppContent() {
  return (
    <NotificationProvider>
      <div className="min-h-screen flex flex-col bg-gray-50">
        <Navbar />
      <main className="flex-1">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/items" element={<ItemsPage />} />
          <Route
            path="/items/create"
            element={
              <ProtectedRoute>
                <ItemCreatePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/items/:id/edit"
            element={
              <ProtectedRoute>
                <ItemEditPage />
              </ProtectedRoute>
            }
          />
          <Route path="/items/:id" element={<ItemDetailPage />} />
          <Route path="/skills" element={<SkillsPage />} />
          <Route
            path="/skills/create"
            element={
              <ProtectedRoute>
                <SkillCreatePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/skills/:id/edit"
            element={
              <ProtectedRoute>
                <SkillEditPage />
              </ProtectedRoute>
            }
          />
          <Route path="/skills/:id" element={<SkillDetailPage />} />
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <ProfilePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/exchanges"
            element={
              <ProtectedRoute>
                <ExchangesPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/notifications"
            element={
              <ProtectedRoute>
                <NotificationsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/messages/:exchangeId"
            element={
              <ProtectedRoute>
                <MessagesPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/reviews/create"
            element={
              <ProtectedRoute>
                <ReviewCreatePage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>
      <Footer />
    </div>
    </NotificationProvider>
  );
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppContent />
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;