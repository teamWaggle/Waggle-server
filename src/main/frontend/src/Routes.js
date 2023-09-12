import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Main from './pages/Main';

import ScrollToTop from '../components/ScrollToTop';

function Router() {
    return (
        <BrowserRouter>
            <ScrollToTop />
            <Routes>
                <Route path="/" element={<Main />} />
            </Routes>
        </BrowserRouter>
    );
}

export default Router;