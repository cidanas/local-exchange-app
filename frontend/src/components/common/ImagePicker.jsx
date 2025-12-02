import { useState } from 'react';
import Button from './Button';
import { uploadService } from '../../services/uploadService';

export default function ImagePicker({ images = [], onChange, maxFiles = 5 }) {
  const [showPicker, setShowPicker] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState('');

  const handleFiles = async (files) => {
    const filesArr = Array.from(files).slice(0, maxFiles - images.length);
    if (filesArr.length === 0) return;
    
    // Show preview with loading state
    const previews = [];
    for (const file of filesArr) {
      const reader = new FileReader();
      reader.onload = (e) => {
        previews.push(e.target.result);
        if (previews.length === filesArr.length) {
          onChange([...images, ...previews]);
        }
      };
      reader.readAsDataURL(file);
    }

    try {
      setUploading(true);
      setUploadError('');
      const urls = await uploadService.upload(filesArr);
      // Replace previews with actual URLs from server
      const newImages = images.filter(img => !img.startsWith('data:'));
      onChange([...newImages, ...urls]);
    } catch (err) {
      console.error('Upload error', err);
      setUploadError('Erreur lors de l\'upload des images');
      // Remove preview images on error
      const newImages = images.filter(img => !img.startsWith('data:'));
      onChange(newImages);
    } finally {
      setUploading(false);
      setShowPicker(false);
    }
  };

  const removeImage = (index) => {
    const next = images.filter((_, i) => i !== index);
    onChange(next);
  };

  return (
    <div>
      {uploadError && (
        <div className="mb-2 p-2 bg-red-100 text-red-700 rounded text-sm">
          {uploadError}
          <button 
            type="button" 
            onClick={() => setUploadError('')}
            className="ml-2 font-bold"
          >
            ×
          </button>
        </div>
      )}
      
      <div className="flex flex-wrap gap-2 mb-2 p-2 bg-gray-50 rounded min-h-[100px]">
        {images && images.length > 0 ? (
          images.map((src, i) => (
            <div key={i} className="relative group">
              <img 
                src={src} 
                alt={`img-${i}`} 
                className="w-24 h-24 object-cover rounded border-2 border-gray-200"
                onError={(e) => {
                  console.error('Image load error:', src);
                  e.target.style.border = '2px solid red';
                }}
              />
              {src.startsWith('data:') && (
                <div className="absolute inset-0 flex items-center justify-center bg-black bg-opacity-50 rounded">
                  <span className="text-white text-xs">Chargement...</span>
                </div>
              )}
              <button
                type="button"
                onClick={() => removeImage(i)}
                className="absolute -top-1 -right-1 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center text-xs hover:bg-red-600"
                title="Supprimer"
              >
                ×
              </button>
            </div>
          ))
        ) : (
          <div className="w-full flex items-center justify-center text-gray-500 text-sm">
            Aucune image sélectionnée
          </div>
        )}
      </div>

      <div className="flex gap-2">
        <Button 
          type="button" 
          onClick={() => setShowPicker(true)} 
          disabled={uploading}
        >
          {uploading ? 'Téléversement...' : 'Choisir des images'}
        </Button>
        <Button 
          type="button" 
          variant="secondary" 
          onClick={() => onChange([])}
          disabled={images.length === 0}
        >
          Supprimer tout
        </Button>
      </div>

      {showPicker && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-lg">
            <h3 className="text-lg font-semibold mb-4">Sélectionnez des images (max {maxFiles})</h3>
            <p className="text-sm text-gray-600 mb-4">
              Sélectionnez un ou plusieurs fichiers image
            </p>
            <input
              type="file"
              accept="image/*"
              multiple
              onChange={(e) => {
                handleFiles(e.target.files);
                e.target.value = null;
              }}
              className="border border-gray-300 rounded w-full p-2 mb-4"
            />
            <div className="mt-4 flex justify-end gap-2">
              <Button 
                type="button" 
                onClick={() => setShowPicker(false)} 
                variant="secondary"
              >
                Fermer
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
