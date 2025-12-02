export default function Card({ children, className = '', hover = false, onClick }) {
  return (
    <div
      onClick={onClick}
      className={`bg-white rounded-xl shadow-md overflow-hidden transition ${
        hover ? 'hover:shadow-xl cursor-pointer' : ''
      } ${className}`}
    >
      {children}
    </div>
  );
}