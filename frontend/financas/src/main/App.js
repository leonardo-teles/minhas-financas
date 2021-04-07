import React from 'react';

import Rotas from './rotas';
import Navbar from '../components/navbar';

import '../custom.css';
import 'bootswatch/dist/flatly/bootstrap.min.css';

class App extends React.Component {
  render(){
    return(
        <>
          <Navbar/>
          <div className="container">
              <Rotas />
          </div>
        </>
    )
  }
}

export default App;
