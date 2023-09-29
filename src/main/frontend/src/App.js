import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Main from './pages/mainPage';
import SignUpPage from './pages/signUpPage';
import SignInPage from './pages/signInPage';
import WriteStory from './pages/writeStoryPage';
import StoryPage from './pages/storyPage';


import ScrollToTop from './components/ScrollToTop';
import './styles/globalStyles.css';



function App() {
    return (
        <BrowserRouter>
            <ScrollToTop />
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/member/sign-up" element={<SignUpPage />} />
                <Route path="/member/sign-in" element={<SignInPage />} />
                <Route path="/story/write" element={<WriteStory />} />  
                <Route path="/story/:username/:boardId" element={<StoryPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;