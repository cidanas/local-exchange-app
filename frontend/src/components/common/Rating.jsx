import { Star } from 'lucide-react';

export default function Rating({ value = 0, max = 5, size = 16, showValue = true, onChange, editable = false }) {
  const handleClick = (rating) => {
    if (editable && onChange) {
      onChange(rating);
    }
  };

  return (
    <div className="flex items-center gap-1">
      {[...Array(max)].map((_, i) => {
        const rating = i + 1;
        const isFilled = rating <= Math.floor(value);
        return (
          <button
            key={i}
            type="button"
            onClick={() => handleClick(rating)}
            disabled={!editable}
            className={editable ? 'cursor-pointer' : 'cursor-default'}
          >
            <Star
              size={size}
              className={isFilled ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'}
            />
          </button>
        );
      })}
      {showValue && <span className="text-sm font-semibold ml-1">{value.toFixed(1)}</span>}
    </div>
  );
}