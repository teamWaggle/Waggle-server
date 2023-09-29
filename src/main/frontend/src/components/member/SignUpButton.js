import React from 'react';

import styles from '../../styles/SignUpButton.module.css';

function SignUpButton({ onClick }) {
    return (
        <button className={styles.button} onClick={onClick}>
            회원가입
        </button>
    );
}

export default SignUpButton;