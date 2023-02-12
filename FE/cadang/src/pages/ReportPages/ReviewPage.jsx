import * as React from "react";
import axios from "axios";
import dayjs from "dayjs";
import { Fragment, useState, useMemo, useEffect } from "react";
import { useHistory, useLocation } from "react-router-dom";
import { Grid, Card } from "@mui/material";
import Typography from "@mui/joy/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

import { userReviewDetail } from "../../api/report";

import ModifyReviewInfo from "../../components/ModifyReviewInfo";
import ReadOnlyCustomOption from "../../components/ReadOnlyCustomOption";

const ReviewPage = () => {
  const history = useHistory()
  const location = useLocation();
  const reviewId = location.state.review.id;
  const originImg = location.state.review.photo;

  const [reviewDetail, setreviewDetail] = useState({
    reviewDetail: [
      {
        id: 0,
        photo: "string",
        drinkName: "string",
        caffeine: 0,
        sugar: 0,
        cal: 0,
        price: 0,
        regDate: "2023-02-08T10:57:49.450Z",
        memo: "string",
        size: "string",
        shot: 0,
        whip: true,
        sugarContent: "LESS",
        syrup: 0,
        vanilla: 0,
        caramel: 0,
        hazelnut: 0,
        orderStatus: "REQUEST",
        efaultUrl: "string",
      },
    ],
  });
  useMemo(() => {
    const getReviewDetails = async () => {
      await userReviewDetail(
        reviewId,
        (res) => {
          return res.data;
        },
        (err) => console.log(err)
      ).then((data) => setreviewDetail(data));
    };
    getReviewDetails();
  }, [reviewId]);

  const recordDate = dayjs(reviewDetail.regDate).format("YYYY-MM-DD");
  const [modifyDate, setmodifyDate] = useState(recordDate);
  const [modifyIsPublic, setmodifyIsPublic] = useState(true);
  const [modifyMemo, setModifyMemo] = useState(reviewDetail.memo);
  const [modifyImage, setImage] = useState({
    image_file: "",
    preview_URL: originImg,
  });
  console.log(reviewDetail);
  console.log(originImg);
  const [isModified, setIsModified] = useState(0);

  /////////날짜 변경 확인
  const getRecordDate = (newValue) => {
    setmodifyDate(newValue);
  };

  // use
  // useEffect(() => {
  //   getRecordDate();
  //   console.log(modifyDate)
  // }, [modifyDate]);

  /////////이미지 변경 확인
  const getImg = (image_file, preview_URL) => {
    const newImage = { image_file, preview_URL };
    setImage(newImage);
  };

  const changeImg = () => {
    setIsModified(1);
    console.log(isModified);
  };

  const deleteImg = () => {
    setIsModified(2);
    console.log(isModified);
  };

  // useEffect(() => {
  //   changeImg();
  //   deleteImg();
  //   console.log(modifyImage);
  // }, [modifyImage]);

  // useEffect(() => {
  //   getImg();
  //   console.log(modifyImage);
  // }, [modifyImage]);

  //리뷰글 변경 확인
  const onChangeMemo = (e) => {
    setModifyMemo(e.target.value);
  };

  const modifyData = {
    id: reviewDetail.id,
    regDate: modifyDate,
    isPublic: true,
    memo: modifyMemo,
    isModified: isModified,
  };

  const formData = new FormData();
  formData.append("image", modifyImage.image_file);
  formData.append("data", JSON.stringify(modifyData));

  console.log(formData);
  console.log(modifyData);

  // const modifyReviewDetailRecord = async () => {
  //   await modifyReviewDetail(
  //     formData,
  //     (res) => console.log(res),
  //     (err) => console.log(err),
  //   ).then((res) => {
  //     if (res.status === 200) {
  //       window.location.reload()
  //     }
  //   });
  // };

  const modifyReviewDetailRecord = async () => {
    await axios
      .put("http://i8a808.p.ssafy.io:8080/record", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization:
          "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmFiMTIzNCIsImlkIjo2OSwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3NjIxODE0NX0.s_6_v2kF_hQ4fc7CwscGr47Koq3kzgcCIROUXMiOmJvvdq4x7Cudns_smA6wgf1TgFxy4S76CxxEyuBwwepixg",
        },
        params: modifyData,
      })
      .then(function (response) {
        console.log(response, "성공");
        if (response.status === 200) {
          history.push('/mypage')
        }
      })
      .catch(function (err) {
        console.log(err);
      });
  };

  return (
    <Fragment>
      <Typography level="h3" fontSize="xl" fontWeight="xl">
        Review
      </Typography>
      <ModifyReviewInfo
        data={reviewDetail}
        image={modifyImage}
        getImg={getImg}
        changeImg={changeImg}
        deleteImg={deleteImg}
        getRecordDate={getRecordDate}
      />
      <Card sx={{ marginTop: 2, height: 110 }}>
        <TextField
          defaultValue={reviewDetail.memo}
          fullWidth
          multiline
          rows={4}
          variant="standard"
          onChange={onChangeMemo}
        />
      </Card>
      <ReadOnlyCustomOption data={reviewDetail} />
      <Grid item>
        <Button fullWidth={true} onClick={modifyReviewDetailRecord}>
          저장하기
        </Button>
      </Grid>
    </Fragment>
  );
};

export default ReviewPage;
