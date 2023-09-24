import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import SignUp from '../components/member/SignUp';
import SignUpButton from '../components/member/SignUpButton';
import { memberRegisterApi } from '../apis/memberApi';

import styles from '../styles/signUpPage.module.css';
import '../styles/globalStyles.css';


function SignUpPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        password: '',
        name: '',
        phone: '',
        address: '',
        profileImg: null,
    });

    const handleRegister = () => {
        console.log('button clicked');
        const payload = {
            ...formData
        };

        memberRegisterApi(payload)
            .then(response => {
                console.log('Registration successful:', response);
                navigate('/');
            })
            .catch(error => {
                console.log('Registration failed:', error);
            });
    };


    const updateFormData = (key, value) => {
        setFormData(prev => ({ ...prev, [key]: value }));
    };

    return (
        <div className="page-container">
            <div className={styles.mainContainer}>
                <Link to="/" className={styles.logo}>
                    <div>Waggle</div>
                </Link>
                <SignUp updateFormData={updateFormData} />
                <SignUpButton onClick={handleRegister} />
            </div>
        </div>
    );
}

export default SignUpPage;