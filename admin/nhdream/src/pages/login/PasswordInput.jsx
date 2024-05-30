import React, { useState, useCallback } from "react";
import "@/assets/css/passwordInput.css"
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const PasswordInput = () => {
  const navigate = useNavigate()
  let numInit = Array.from({ length: 10 }, (_, k) => k);
  const PASSWORD_MAX_LENGTH = 6;
  const [number, setNumber] = useState(numInit);
  const [password, setPassword] = useState("");

  const handlePasswordChange = useCallback((num) => {
    if (password.length < PASSWORD_MAX_LENGTH) {
      setPassword(password + num.toString());
    }
  }, [password]);

  const erasePasswordOne = useCallback(() => {
    setPassword(password.slice(0, -1));
  }, [password]);

  const erasePasswordAll = useCallback(() => {
    setPassword("");
  }, []);

  const shuffle = (numInit) => {
    let numLength = numInit.length;
    while (numLength) {
      let randomIndex = Math.floor(numLength-- * Math.random());
      let temp = numInit[randomIndex];
      numInit[randomIndex] = numInit[numLength];
      numInit[numLength] = temp;
    }
    return numInit;
  };

  const shuffleNumber = useCallback((num) => () => {
    setNumber(shuffle([...numInit]));
    handlePasswordChange(num);
  }, [handlePasswordChange]);

  const onClickSubmitButton = () => {
    if (password.length === 0) {
      Swal.fire("비밀번호를 입력해주세요.");
    } else if (password.length === PASSWORD_MAX_LENGTH) {
      Swal.fire("오늘도 즐거운 하루 보내세요")
      navigate('/')
      console.log(password)
    } else {
      console.log(password)
      Swal.fire("틀렸습니다!")
    }
  };

  return (
    <>
    <div className="flex justify-center">

      <input className='password-container ' type='password' value={password} readOnly />
    </div>
      <div className='inputter__flex'>
        {number.map((n, index) => (
          <button
            className='num-button__flex spread-effect fantasy-font__2_3rem'
            onClick={shuffleNumber(n)}
            key={index}
          >
            {n}
          </button>
        ))}
        <button
          className='num-button__flex spread-effect fantasy-font__2_3rem'
          onClick={erasePasswordAll}
        >
          X
        </button>
        <button
          className='num-button__flex spread-effect fantasy-font__2_3rem'
          onClick={erasePasswordOne}
        >
          ←
        </button>
      </div>
      <div>
        <button type='submit' className='submit-button' onClick={onClickSubmitButton}>
          Submit
        </button>
      </div>
    </>
  );
};

export default PasswordInput;
