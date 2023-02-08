import React, { useState, useEffect } from "react"
import MenuListItem from "./MenuListItem"
import Switch from "@mui/material/Switch"
import FormControlLabel from "@mui/material/FormControlLabel"
import FormControl from "@mui/material/FormControl"
import NativeSelect from "@mui/material/NativeSelect"
import { Paper, Grid, Divider, Card, List } from "@mui/material"
import Typography from "@mui/material/Typography"

function ItemFiltering(props) {
  // console.log(props)
  // console.log(">>>>>>>>>>>>", props.menus.drinkList[0])
  const menuDatas = props.menus.drinkList[0]
  console.log('>>>>>>>>>>props 받아오는 거>>>',menuDatas)
  // const props.menus
  // const rawData = props.data
  // const [data, setData] = useState([])
  const [menu, setMenu] = useState([])
  const [sortType, setSortType] = useState("caffeine")

  useEffect(() => {
    const sortArray = (type) => {
      const types = {
        caffeine: "caffeine",
        sugar: "sugar",
        cal: "cal",
        price: "price",
      }
      const sortProperty = types[type]
      const sorted = [...menuDatas].sort(
        (a, b) => a[sortProperty] - b[sortProperty]
      )
      // console.log(sorted)
      setMenu(sorted)
    }

    sortArray(sortType)
  }, [sortType])

  return (
    <Card>
      <Grid
        container
        sx={{ display: "flex" }}
        justifyContent="end"
        alignItems="center"
      >
        <Grid item>
          <FormControlLabel
            control={<Switch defaultChecked />}
            label="되는 음료만 보기"
            labelPlacement="end"
            margin="0"
          />

          <FormControl size="small" sx={{ paddingTop: 1 }}>
            <NativeSelect
              defaultValue="caffeine"
              onChange={(e) => setSortType(e.target.value)}
            >
              <option value="caffeine">낮은 카페인순</option>
              <option tem value="sugar">
                낮은 당순
              </option>
              <option value="cal">낮은 칼로리순</option>
              <option value="price">낮은 가격순</option>
            </NativeSelect>
          </FormControl>
        </Grid>
        {menuDatas.map(menu => {
          return (
            <Grid item>
              <Typography>
                {menu.name}
                {menu.caffeine}
                {menu.sugar}
                {menu.cal}
                {menu.price}
              </Typography>
              {/* 
              <MenuListItem data={data} />
              <MenuListItem data={data} />
              <MenuListItem data={data} />
              <MenuListItem data={data} />
              <MenuListItem data={data} /> */}
            </Grid>
          )
        })}
      </Grid>
    </Card>
  )
}

const menuData = [
  { pk: 1, name: "카페라떼", caffeine: 300, sugar: 10, cal: 200, price: 3000 },
  {
    pk: 2,
    name: "바닐라 라떼",
    caffeine: 200,
    sugar: 20,
    cal: 300,
    price: 5000,
  },
  {
    pk: 3,
    name: "아이스 아메리카노",
    caffeine: 100,
    sugar: 30,
    cal: 100,
    price: 4500,
  },
]

export default ItemFiltering
