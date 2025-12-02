import { CheckCircle, XCircle, AlertCircle, Info, X } from 'lucide-react';

export default function Alert({ type = 'info', message, onClose }) {
  const types = {
    success: { 
      bg: 'bg-green-50', 
      border: 'border-green-200', 
      text: 'text-green-800', 
      icon: CheckCircle 
    },
    error: { 
      bg: 'bg-red-50', 
      border: 'border-red-200', 
      text: 'text-red-800', 
      icon: XCircle 
    },
    warning: { 
      bg: 'bg-yellow-50', 
      border: 'border-yellow-200', 
      text: 'text-yellow-800', 
      icon: AlertCircle 
    },
    info: { 
      bg: 'bg-blue-50', 
      border: 'border-blue-200', 
      text: 'text-blue-800', 
      icon: Info 
    },
  };

  const { bg, border, text, icon: Icon } = types[type];

  return (
    <div className={`${bg} border ${border} rounded-lg p-4 flex items-start gap-3 mb-4`}>
      <Icon className={text} size={20} />
      <p className={`${text} flex-1`}>{message}</p>
      {onClose && (
        <button onClick={onClose} className={`${text} hover:opacity-70`}>
          <X size={18} />
        </button>
      )}
    </div>
  );
}