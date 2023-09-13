import React from 'react';
import { Link } from 'react-router-dom';
import SignIn from '../components/member/SignIn';

import styles from '../styles/signInPage.module.css';
import '../styles/globalStyles.css';
import SignInButton from '../components/member/SignInButton';

function SignInPage() {
    return (
        <div className="page-container">
            <div className={styles.mainContainer}>
                <Link to="/" className={styles.logo}>
                    <div>Waggle</div>
                </Link>
                <SignIn />
                <SignInButton />
            </div>
        </div>
    );
}

export default SignInPage;