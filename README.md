# 🌍 Local Exchange

**Plateforme d'échange local simple et rapide**

---

## ⚡ Démarrage (3 étapes)

### 1️⃣ Vérifier MySQL (localhost:3306)

```bash
mysql -u root -p
# Si erreur: net start MySQL80 (Windows)
```

### 2️⃣ Lancer le Backend

```bash
cd backend
mvn spring-boot:run
```

### 3️⃣ Lancer le Frontend

**Nouveau terminal:**
```bash
cd frontend
npm run dev
```

---

## 🔗 Accès

- **Frontend:** http://localhost:5173
- **API:** http://localhost:8080

---

## 🗑️ Supprimer les Utilisateurs de Test

**[Voir SUPPRIMER_USERS.md](SUPPRIMER_USERS.md)**

(phpMyAdmin → Cochez les 3 users → Supprimer)
