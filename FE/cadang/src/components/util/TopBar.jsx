import * as React from "react";
import './TopBar.css';
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Box from "@mui/material/Box";
import CssBaseline from "@mui/material/CssBaseline";
import logo from "../../assets/logo.png";


export default function TopBar() {
  const ref = React.useRef(null);

  return (
    <Box sx={{ pb: 7 }} ref={ref}>
      <CssBaseline />
      <AppBar className="appBar" position="static">
        <Toolbar>
          <img height={65} src={logo} alt="로고" />
        </Toolbar>
      </AppBar>
    </Box>
  );
}