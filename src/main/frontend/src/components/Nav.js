import { Link } from 'react-router-dom';
import React from 'react';

import SearchBar from './main/SearchBar';

import styles from '../styles/Nav.module.css';


function Nav() {
    return (
        <div className={styles.navbar}>
            <Link className={styles.logo} to={'/'}>Waggle</Link>
            <SearchBar />
            <div className={styles.navbarMenu}>
                <Link className={styles.navbarMenuItem} to={'/qna'}>Q&A</Link>
                <Link className={styles.navbarMenuItem} to={'/schedule'}>일정</Link>
                <Link className={styles.navbarMenuItem} to={'/map'}>지도</Link>
                <Link className={styles.navbarMenuItem} to={'/mypage'}>마이페이지</Link>
            </div>
        </div>
    );
}

export default Nav;