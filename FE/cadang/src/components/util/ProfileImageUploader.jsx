import { useEffect, useState } from "react"
import { Button } from "@mui/material"
// import axios from "axios"
import default_image from "../../assets/default_image.png"
import styled from "styled-components"
import AddAPhotoIcon from "@mui/icons-material/AddAPhoto"

const ProfileImageUploader = ({ getImg }) => {
  const [imagestatus, setImageStatus] = useState({
    image_file: "",
    preview_URL: default_image,
  })

  //컴포넌트 이름 바꾸기

  let inputRef

  useEffect(
    function () {
      getImg(imagestatus.image_file, imagestatus.preview_URL)
    },
    [imagestatus]
  )

  const saveImage = (e) => {
    e.preventDefault()
    if (e.target.files[0]) {
      // 새로운 이미지를 올리면 createObjectURL()을 통해 생성한 기존 URL을 폐기
      URL.revokeObjectURL(imagestatus.preview_URL)
      const preview_URL = URL.createObjectURL(e.target.files[0])
      setImageStatus(() => ({
        image_file: e.target.files[0],
        preview_URL: preview_URL,
      }))
    }
  }

  const deleteImage = () => {
    // createObjectURL()을 통해 생성한 기존 URL을 폐기
    URL.revokeObjectURL(imagestatus.preview_URL)
    setImageStatus({
      image_file: "",
      preview_URL: default_image,
    })
  }

  useEffect(() => {
    // 컴포넌트가 언마운트되면 createObjectURL()을 통해 생성한 기존 URL을 폐기
    return () => {
      URL.revokeObjectURL(imagestatus.preview_URL)
    }
  }, [])

  return (
    <UploaderWrapper className="uploader-wrapper">
      <input
        type="file"
        accept="image/*"
        onChange={saveImage}
        // 클릭할 때 마다 file input의 value를 초기화 하지 않으면 버그가 발생할 수 있다
        // 사진 등록을 두개 띄우고 첫번째에 사진을 올리고 지우고 두번째에 같은 사진을 올리면 그 값이 남아있음!
        onClick={(e) => (e.target.value = null)}
        ref={(refParam) => (inputRef = refParam)}
        style={{ display: "none" }}
      />
      <ImgWrapper className="img-wrapper">
        <ImgSpace
          src={imagestatus.preview_URL}
          alt="img"
          onClick={() => inputRef.click()}
          style={{
            borderRadius: "100%",
            width: "100px",
            height: "100px",
            objectFit: "cover",
          }}
        />
      </ImgWrapper>
      <div className="upload-button">
        <IconContainer>
          <AddAPhotoIcon variant="contained"></AddAPhotoIcon>
        </IconContainer>
        <DeleteButton color="error" variant="contained" onClick={deleteImage}>
          기본이미지로 변경
        </DeleteButton>
      </div>
    </UploaderWrapper>
  )
}

const UploaderWrapper = styled.div`
  position: relative;
  height: 100%;
  width: 100%;
`
const ImgWrapper = styled.div`
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 100px;
  margin-top: 7px;
  margin-left: 10px;
`
const ImgSpace = styled.img`
  width: 100%;
`
const DeleteButton = styled.button`
  grid-area: DeleteButton;
  margin-left: 8px;
  border-radius: 5px !important;
  border: 2px solid #674f04 !important;
  background-color: #ffffff !important;
  font-family: netmarble;
  color: 350B !important;
`
const IconContainer = styled.div`
  position: absolute;
  bottom: 40px;
  left: 75px;
  z-index: 1;
  background-color: white;
  border-radius: 20px !important;
  border: 2px solid black !important;
  width: 33px;
  height: 33px;
  padding: 3px;
`

export default ProfileImageUploader
