import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './styles/styles.css';
import HomePage from "./pages/HomePage/HomePage";
import LoginPage from "./pages/LoginPage/LoginPage";
import SignupPage from "./pages/SignupPage/SignupPage";
import LogoutPage from "./pages/LogoutPage";
import CategoryPage from './pages/CategoryPage/CategoryPage';
import PostPage from './pages/PostPage/PostPage';
import AddPostPage from './pages/AddPostPage/AddPostPage';
import UserPage from './pages/UserPage/UserPage';
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";

const App = () => {
  return (
    <Router>
      <div className="app-wrapper">
        <Navbar />
        <div className="container">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignupPage />} />
            <Route path="/logout" element={<LogoutPage />} />
            <Route path="/category/:categoryId" element={<CategoryPage />} />
            <Route path="/post/:postId" element={<PostPage />} />
            <Route path="/post/add/:categoryId?" element={<AddPostPage />} />
            <Route path="/user/:userId" element={<UserPage />} />
          </Routes>
        </div>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
