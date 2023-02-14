import React, { useEffect, useRef, useState, useMemo } from "react";
import PropTypes from "prop-types";
import { Paper, Box, Grid, Card } from "@mui/material";
import Typography from "@mui/joy/Typography";
import OrderListItem from "./OrderListItem";
import { orderList } from "../api/cafeCeo";
import SockJsClient from "react-stomp";

export default function OrderList() {
  const [orderListData, setOrderListData] = useState([]);
  const $websocket = useRef();

  useMemo(() => {
    // 화면 랜더링 되기 전 현재 주문 목록 리스트 통신
    const getOrderList = async () => {
      await orderList(
        (res) => {
          console.log(res.data);
          return res.data;
        },
        (err) => console.log(err)
      ).then((data) => setOrderListData(data));
    };
    getOrderList();
  }, []);

  useEffect(() => {}, [orderListData]);



  return (
    <div>
      <SockJsClient
        url="http://i8a808.p.ssafy.io:8080/websocket"
        headers={{
          Authorization:
            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdGFyYnVja3MiLCJpZCI6MSwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY3NjQyMDE4M30.5fIcQRXCRCaddt8ekcw-8BIHHA7I6zQwUgQXy85yE9gbJ2UGBumnCyMssleEHg7G6XNW2fCHSKDuDKbB9PKiCA",
        }}
        // topics={["/topic/store-order-manage/1", ""]}
        onMessage={(msg) => {
          // console.log(msg);
          // setMsg(msg);
        }}
        ref={$websocket}
      />
      {orderListData.map((item, key) => (
        <OrderListItem order={item} id={key} />
      ))}
    </div>
  );
}
