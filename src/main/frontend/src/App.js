import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Main from './pages/Main';
import Story from './pages/Story';
import WriteStory from './pages/WriteStory';

import ScrollToTop from './components/ScrollToTop';

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