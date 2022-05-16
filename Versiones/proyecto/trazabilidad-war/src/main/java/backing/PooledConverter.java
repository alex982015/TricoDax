package backing;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import jpa.PooledAccount;

@SuppressWarnings("rawtypes")
@FacesConverter(value = "pooledConverter")
public class PooledConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ValueExpression vex =
				context.getApplication().getExpressionFactory()
                        .createValueExpression(context.getELContext(),
                                "#{cuentasAdmin}", CuentasAdmin.class);

        CuentasAdmin cuentas = (CuentasAdmin)vex.getValue(context.getELContext());
        return cuentas.getPooledAccount(value);
	}
	
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object pooled) {
		 return ((PooledAccount)pooled).getIBAN();
	}

}
