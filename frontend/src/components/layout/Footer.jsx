export default function Footer() {
  return (
    <footer className="bg-gray-800 text-white py-8 mt-20">
      <div className="max-w-7xl mx-auto px-4">
        <div className="grid md:grid-cols-3 gap-8">
          <div>
            <h3 className="text-xl font-bold mb-4">LocalExchange</h3>
            <p className="text-gray-400">
              Partagez vos biens et compétences avec votre communauté locale.
            </p>
          </div>
          <div>
            <h4 className="font-semibold mb-4">Liens rapides</h4>
            <ul className="space-y-2 text-gray-400">
              <li><a href="/items" className="hover:text-white transition">Objets</a></li>
              <li><a href="/skills" className="hover:text-white transition">Compétences</a></li>
              <li><a href="/about" className="hover:text-white transition">À propos</a></li>
              <li><a href="/contact" className="hover:text-white transition">Contact</a></li>
            </ul>
          </div>
          <div>
            <h4 className="font-semibold mb-4">Suivez-nous</h4>
            <p className="text-gray-400">
              © 2024 LocalExchange. Tous droits réservés.
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
}