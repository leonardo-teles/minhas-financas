import React from 'react';

import Card from '../components/card';
import FormGroup from '../components/form-group';

class Login extends React.Component {
    render() {
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6" style={ {position: 'relative', left: '300px'} }>
                        <div className="bs-docs-section">
                            <Card title="Login">
                                <div className="row">
                                    <div className="col-lg-12">
                                        <div className="bs-component">
                                            <fieldset>

                                                <FormGroup label="e-Mail: *" htmlFor="email">   
                                                    <input type="email" 
                                                           className="form-control" 
                                                           id="email" 
                                                           aria-describedby="email" 
                                                           placeholder="Digite o e-Mail"
                                                    />
                                                </FormGroup>

                                                <FormGroup label="Senha: *" htmlFor="senha">   
                                                    <input type="password" 
                                                           className="form-control" 
                                                           id="senha" 
                                                           aria-describedby="senha" 
                                                           placeholder="Digite a senha"
                                                    />
                                                </FormGroup>

                                            </fieldset>
                                        </div>
                                    </div>
                                </div>
                            </Card>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default Login;