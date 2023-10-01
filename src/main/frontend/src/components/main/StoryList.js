import React, { useState, useEffect } from 'react';
import SortTab from './SortTab';
import { getAllStoriesApi } from '../../apis/storyApi';
import SimpleStory from './SimpleStory';
import styles from '../../styles/StoryList.module.css';

const StoryList = () => {
  const [sortBy, setSortBy] = useState('popular');
  const [stories, setStories] = useState([]);

  useEffect(() => {
    fetchStories();
  }, [sortBy]);

  const fetchStories = async () => {
    try {
      const response = await getAllStoriesApi();
      const data = response.data;
      setStories(data);
    } catch (error) {
      console.error('API 요청 오류:', error);
    }
  };

  const handleSortChange = (newSortBy) => {
    setSortBy(newSortBy);
  };

  const chunkSize = 3;
  const chunkedStories = [];
  for (let i = 0; i < stories.length; i += chunkSize) {
    chunkedStories.push(stories.slice(i, i + chunkSize));
  }

  return (
    <div className={styles.storyList}>
      <SortTab onSortChange={handleSortChange} />
      <div className={styles.storyGrid}>
        {chunkedStories.map((chunk, index) => (
          <div className={styles.storyRow} key={index}>
            {chunk.map((story) => (
              <div className={styles.storyColumn} key={story.id}>
                <SimpleStory story={story} />
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
};

export default StoryList;
