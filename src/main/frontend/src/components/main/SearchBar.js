import React from 'react';

import styles from '../../styles/SearchBar.module.css';


function SearchBar() {
    return (
        <div className={styles.searchBar}>
            <input type="text" placeholder="검색어를 입력해보세요." />
            <img src="https://s3.ap-northeast-2.amazonaws.com/cdn.wecode.co.kr/icon/search.png" alt="검색" />
        </div>
    );
}

export default SearchBar;