import React, { useState } from 'react';

import styles from '../../styles/SortTab.module.css'


const SortTab = ({ onSortChange }) => {
  const [activeTab, setActiveTab] = useState('popular');

  const handleTabClick = (tab) => {
    console.log('clicked : ', tab);
    setActiveTab(tab);
    onSortChange(tab);
  };

  return (
    <div className={styles.sortTab}>
      <span
        className={`${activeTab === 'popular' ? styles.active : ''
          } ${styles.tabItem}`}
        onClick={() => handleTabClick('popular')}
      >
        인기순
      </span>
      <span
        className={`${activeTab === 'latest' ? styles.active : ''
          } ${styles.tabItem}`}
        onClick={() => handleTabClick('latest')}
      >
        최신순
      </span>
    </div>
  );
};

export default SortTab;
