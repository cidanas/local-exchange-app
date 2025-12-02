import { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { Send } from 'lucide-react';
import { messageService } from '../services/messageService';
import { useAuth } from '../context/AuthContext';
import Card from '../components/common/Card';
import Button from '../components/common/Button';
import LoadingSpinner from '../components/common/LoadingSpinner';

export default function MessagesPage() {
  const { exchangeId } = useParams();
  const { user } = useAuth();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    loadMessages();
    const interval = setInterval(loadMessages, 5000); // Rafraîchir toutes les 5s
    return () => clearInterval(interval);
  }, [exchangeId]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const loadMessages = async () => {
    try {
      const res = await messageService.getConversation(exchangeId);
      setMessages(res.data || []);
    } catch (error) {
      console.error('Error loading messages:', error);
    } finally {
      setLoading(false);
    }
  };

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  const handleSend = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    try {
      await messageService.send({
        contenu: newMessage,
        exchangeRequestId: parseInt(exchangeId),
      });
      setNewMessage('');
      loadMessages();
    } catch (error) {
      console.error('Error sending message:', error);
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-6">Messagerie</h1>

      <Card className="h-[600px] flex flex-col">
        {/* Messages */}
        <div className="flex-1 overflow-y-auto p-6 space-y-4">
          {messages.length === 0 ? (
            <div className="text-center text-gray-500 py-12">
              Aucun message. Commencez la conversation !
            </div>
          ) : (
            messages.map((message) => {
              const isOwn = message.expediteurId === user?.id;
              return (
                <div
                  key={message.id}
                  className={`flex ${isOwn ? 'justify-end' : 'justify-start'}`}
                >
                  <div
                    className={`max-w-[70%] rounded-lg p-4 ${
                      isOwn
                        ? 'bg-indigo-600 text-white'
                        : 'bg-gray-100 text-gray-800'
                    }`}
                  >
                    <p className="text-sm font-semibold mb-1">
                      {message.expediteurNom}
                    </p>
                    <p className="whitespace-pre-wrap">{message.contenu}</p>
                    <p
                      className={`text-xs mt-2 ${
                        isOwn ? 'text-indigo-200' : 'text-gray-500'
                      }`}
                    >
                      {new Date(message.createdAt).toLocaleString('fr-FR')}
                    </p>
                  </div>
                </div>
              );
            })
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Zone d'envoi */}
        <form onSubmit={handleSend} className="border-t p-4">
          <div className="flex gap-2">
            <input
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="Écrivez votre message..."
              className="flex-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
              maxLength={2000}
            />
            <Button type="submit" disabled={!newMessage.trim()}>
              <Send size={20} />
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
}