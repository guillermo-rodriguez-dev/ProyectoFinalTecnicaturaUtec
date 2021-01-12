package managedBean;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.List;
import java.util.Objects;

/**
 * 
 * Esta clase conversor generico de entidades para nuestro picklist
 * 
 * 
 * @author: G13 S.A
 * 
 * @version: 28/06/2020
 * 
 * @see <a href = "www.utec.edu.uy" />
 * 
 */

@ManagedBean(name = "converter")
@SessionScoped
public class PicklistGenericConverter implements Converter
{
  @Override
  public String getAsString(FacesContext context, UIComponent component, Object entity)
  {
    if (entity == null)
      return "";
    return String.valueOf(entity.hashCode());
  }

  @Override
  public Object getAsObject(FacesContext context, UIComponent component, String uuid)
  {
    Object ret = null;
    if (uuid == null || uuid.equals(""))
      return null;

    if (component instanceof PickList)
    {
      final Object dualList = ((PickList) component).getValue();
      final DualListModel dl = (DualListModel) dualList;
      ret = retrieveObject(dl.getSource(), uuid);
      if (ret == null)
        ret = retrieveObject(dl.getTarget(), uuid);
    }

    return ret;
  }

 
  @SuppressWarnings("unchecked")
  private Object retrieveObject(final List objects, final String uuid)
  {
    return objects
      .stream()
      .filter(Objects::nonNull)
      .filter(obj -> uuid.equals(String.valueOf(obj.hashCode())))
      .findFirst()
      .orElse(null);
  }
}
