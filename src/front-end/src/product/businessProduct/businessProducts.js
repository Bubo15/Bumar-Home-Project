import businessService from "../../service/business";
import React, {useEffect, useState} from "react";
import Header from "../../header/header";
import Footer from "../../footer/footer";
import Product from "../product/product";
import './businessProducts.css'
import Grid from "@material-ui/core/Grid";
import DeleteIcon from "@material-ui/core/SvgIcon/SvgIcon";

const BusinessProductList = (props) => {

    async function asyncCall() {
        return await new Promise((setProducts) => {
            const id = document.location.pathname.substr(10).split('/')[0];

            businessService.getAllBusiness
                .then(data => {
                    const business = data.data['_embedded'].businessResponseModelList.filter(b => Number(b.id) === Number(id));

                    JSON.stringify(business) !== JSON.stringify([]) ?
                    businessService.getAllProductByBusiness(business[0]['_links']['business-product'].href, props.token)
                        .then(data => {
                            setProducts(data.data)
                        })
                        : document.location.pathname = "/api/business"
                });

        }, []);
    }

    const [products, setProducts] = useState([]);

    useEffect(() => {

        const newVal = async () => {
            await asyncCall().then(setProducts);
        };

        newVal()
    }, []);

    return (
        <div>
            <Header/>

            <div>
                <Grid container spacing={24}>
                    {products.map(p => {
                        return (
                            <Grid item md={3}>
                                <Product id={p.id} name={p.name} price={p.price} mainDescription={p.mainDescription}
                                         img={p.picture.url} countOfProduct={p.countOfProduct}/>
                            </Grid>
                        );
                    })}
                </Grid>
            </div>

            <div className="businessProducts-footer">
                <Footer/>
            </div>
        </div>
    )
};

export default BusinessProductList;