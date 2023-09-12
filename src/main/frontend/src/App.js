import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Main from './pages/mainPage';
import Story from './pages/storyPage';
import WriteStory from './pages/writeStoryPage';

import ScrollToTop from './components/ScrollToTop';
import './styles/globalStyles.css';


function App() {
    return (
        <BrowserRouter>
            <ScrollToTop />
            <Routes>
                <Route path="/" element={<Main />} />
                <Route path="/story" element={<Story />} />
                <Route path="/story/write" element={<WriteStory />} />  
            </Routes>
        </BrowserRouter>
    );
}

export default App;