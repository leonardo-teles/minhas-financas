import React from 'react';
import { withRouter } from 'react-router-dom';

import Card from '../components/card';
import FormGroup from '../components/form-group';

class ConsultaLancamentos extends React.Component {

    render() {
        return(
            <Card title="Consultar Lançamentos">
                <div className="row">
                    <div className="col-md-6">
                        <div className="bs-component">
                            <FormGroup htmlFor="inputAno" label="Ano: *">
                                <input 
                                    type="text" 
                                    class="form-control" 
                                    id="inputAno" 
                                    aria-describedby="emailHelp" 
                                    placeholder="Digite o Ano"/>
                            </FormGroup>
                        </div>
                    </div>
                </div>
            </Card>
        )
    }

}

export default withRouter(ConsultaLancamentos);