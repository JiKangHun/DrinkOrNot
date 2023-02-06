import React, { useState } from "react"
import { Link, useHistory } from "react-router-dom"
import axios from "axios"
import {
  Button,
  CssBaseline,
  TextField,
  FormControl,
  // FormControlLabel,
  // Checkbox,
  FormHelperText,
  Grid,
  Box,
  Typography,
  Container,
} from "@mui/material/"
import { createTheme, ThemeProvider } from "@mui/material/styles"
import styled from "styled-components"
import "./RegisterPage.css"
import ImageUploader from "../../components/util/imageuploader"
import default_image from "../../assets/default_image.png"

// mui의 css 우선순위가 높기때문에 important를 설정 - 실무하다 보면 종종 발생 우선순위 문제
const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 700 !important;
  color: #d32f2f !important;
`

const Boxs = styled(Box)`
  padding-bottom: 10px !important;
`

const RegisterPage = () => {
  const theme = createTheme()
  // const [checked, setChecked] = useState(false)
  const [username, setUserName] = useState("")
  const [memberId, setMemberId] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [passwordState, setpasswordState] = useState("")
  const [nickname, setNickname] = useState("")

  const [usernameError, setUserNameError] = useState("")
  const [memberIdError, setMemberIdError] = useState("")
  const [emailError, setEmailError] = useState("")
  const [passwordStateError, setPasswordStateError] = useState("")
  const [passwordError, setPasswordError] = useState("")
  const [nicknameError, setNicknameError] = useState("")
  const [registerError, setRegisterError] = useState("")
  const history = useHistory()

  // const handleAgree = (event) => {
  //   setChecked(event.target.checked)
  // }

  const [image, setImage] = useState()

  const getImg = (image_file, preview_URL) => {
    const newImage = { image_file, preview_URL }
    setImage(newImage)
  }

  const onhandlePost = async (data) => {
    const { memberId, email, username, nickname, password } = data

    const postData = {
      username: username,
      memberId: memberId,
      password: password,
      email: email,
      nickname: nickname,
    }

    const formData = new FormData()
    formData.append("img", image.image_file)
    formData.append("data", JSON.stringify(postData))

    // post
    await axios
      .post("http://i8a808.p.ssafy.io:8080/user/join", formData, {
        headers: { "Content-Type": "multipart/form-data" },
        params: postData,
      })
      .then(function (response) {
        console.log(response, "성공")
        history.push("/main")
      })
      .catch(function (err) {
        console.log(err)
        setRegisterError("회원가입에 실패하였습니다. 다시 한 번 확인해 주세요.")
      })
  }
  // 이름 유효성 검사
  const usernameRegex = /^[가-힣a-zA-Z]+$/
  const onChangeUserName = (e) => {
    if (
      !e.target.value ||
      usernameRegex.test(e.target.value) ||
      username.length < 1
    )
      setUserNameError(false)
    else setUserNameError("올바른 이름을 입력해주세요.")
    setUserName(e.target.value)
  }

  // 아이디 유효성 체크
  const idRegex = /^[a-zA-Z0-9]+$/
  const onChangeUserId = (e) => {
    if (!e.target.value || idRegex.test(e.target.value) || memberId.length < 1)
      setMemberIdError(false)
    else setMemberIdError("영문자+숫자 조합으로 입력해주세요.")
    setMemberId(e.target.value)
  }

  // 비밀번호 유효성 체크
  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,20}$/
  const onChangePassword = (e) => {
    if (!e.target.value || passwordRegex.test(e.target.value))
      setPasswordError(false)
    else setPasswordError("숫자+영문자 조합으로 8~20자리로 입력해주세요.")

    if (!passwordState || e.target.value === passwordState)
      setPasswordStateError(false)
    else setPasswordStateError("비밀번호가 일치하지 않습니다.")
    setPassword(e.target.value)
  }

  // 비밀번호 같은지 체크
  const onChangePasswordState = (e) => {
    if (password === e.target.value) setPasswordStateError(false)
    else setPasswordStateError("비밀번호가 일치하지 않습니다.")
    setpasswordState(e.target.value)
  }

  // 이메일 유효성 체크
  const emailRegex =
    /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/
  const onChangeEmail = (e) => {
    if (!e.target.value || emailRegex.test(e.target.value)) setEmailError(false)
    else setEmailError("올바른 이메일 형식이 아닙니다.")
    setEmail(e.target.value)
  }

  // 닉네임 유효성 검사
  const nicknameRegex = /^[가-힣a-zA-Z0-9]+$/
  const onChangeNickname = (e) => {
    if (
      !e.target.value ||
      nicknameRegex.test(e.target.value) ||
      nickname.length < 1
    )
      setNicknameError(false)
    else setNicknameError("한글, 영어, 숫자만 사용 가능합니다.")
    setNickname(e.target.value)
  }

  const handleSubmit = (e) => {
    e.preventDefault()

    const data = new FormData(e.currentTarget)
    const joinData = {
      username: data.get("username"),
      memberId: data.get("memberId"),
      password: data.get("password"),
      passwordState: data.get("passwordState"),
      email: data.get("email"),
      nickname: data.get("nickname"),
    }
    const { username, memberId, password, passwordState, email, nickname } =
      joinData

    // 회원가입 동의 체크
    // if (!checked) alert("회원가입 약관에 동의해주세요.")

    if (
      idRegex.test(memberId) &&
      emailRegex.test(email) &&
      passwordRegex.test(password) &&
      password === passwordState &&
      usernameRegex.test(username) &&
      nicknameRegex.test(nickname)
      // checked
    ) {
      onhandlePost(joinData)
    }
  }

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 2,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Typography component="h1" variant="h5">
            회원가입
          </Typography>
          <ImageUploader getImg={getImg}></ImageUploader>
          <Boxs
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 2 }}
          >
            <FormControl component="fieldset" variant="standard">
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="username"
                    name="username"
                    label="이름"
                    // error={usernameError !== "" || false}
                    onChange={onChangeUserName}
                  />
                </Grid>
                <FormHelperTexts>{usernameError}</FormHelperTexts>
                <Grid item xs={9}>
                  <TextField
                    required
                    fullWidth
                    id="memberId"
                    name="memberId"
                    label="아이디"
                    // error={memberIdError !== "" || false}
                    onChange={onChangeUserId}
                  />
                </Grid>
                <FormHelperTexts>{memberIdError}</FormHelperTexts>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    type="password"
                    id="password"
                    name="password"
                    label="비밀번호 (숫자+영문자+특수문자 8자리 이상)"
                    // error={passwordError !== "" || false}
                    onChange={onChangePassword}
                  />
                </Grid>
                <FormHelperTexts>{passwordError}</FormHelperTexts>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    type="password"
                    id="passwordState"
                    name="passwordState"
                    label="비밀번호 재입력"
                    // error={passwordStateError !== "" || false}
                    onChange={onChangePasswordState}
                  />
                </Grid>
                <FormHelperTexts>{passwordStateError}</FormHelperTexts>
                <Grid container xs={12} spacing={2}>
                  <Grid item xs={9}>
                    <TextField
                      required
                      fullWidth
                      type="email"
                      id="email"
                      name="email"
                      label="이메일 주소"
                      onChange={onChangeEmail}
                    />
                  </Grid>
                  <Grid item xs={3}>
                    <SendButton>인증번호 보내기</SendButton>
                  </Grid>
                </Grid>
                <FormHelperTexts>{emailError}</FormHelperTexts>
                <Grid item xs={12}>
                  <TextField
                    required
                    fullWidth
                    id="nickname"
                    name="nickname"
                    label="닉네임"
                    // error={nicknameError !== "" || false}
                    onChange={onChangeNickname}
                  />
                </Grid>
                <FormHelperTexts>{nicknameError}</FormHelperTexts>
                {/* <Grid item xs={12}>
                  <FormControlLabel
                    control={
                      <Checkbox onChange={handleAgree} color="primary" />
                    }
                    label="회원가입 약관에 동의합니다."
                  />
                </Grid> */}
              </Grid>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3 }}
                size="large"
              >
                가입하기
              </Button>
            </FormControl>
            <FormHelperTexts>{registerError}</FormHelperTexts>
            <Button component={Link} to="/sign-in" variant="text">
              이미 회원이라면? 로그인 하러 가기
            </Button>
          </Boxs>
        </Box>
      </Container>
    </ThemeProvider>
  )
}

const SendButton = styled.button`
  background-color: #ffffff;
`

export default RegisterPage
