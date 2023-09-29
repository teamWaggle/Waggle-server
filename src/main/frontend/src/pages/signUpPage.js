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
        nickname: '',
        phone: '',
        address: '',
        profileImg: null,
    });

    const handleRegister = async () => {
        console.log('button clicked');

        try {
            const data = new FormData();
            Object.keys(formData).forEach(key => {
                if(key !== 'profileImg') data.append(key, formData[key]);
            });
            if(formData.profileImg) {
                data.append('profileImg', formData.profileImg);
            }
        
            
            data.append('signUpDto', new Blob([JSON.stringify({
                username: formData.username,
                password: formData.password,
                nickname: formData.nickname,
                phone: formData.phone,
                address: formData.address,
            })], { type: 'application/json' }));

            const response = await memberRegisterApi(data);
            console.log('Registration successful:', response);
            navigate('/member/sign-in');
        } catch(error) {
            console.log('Registration failed:', error);
        }
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
