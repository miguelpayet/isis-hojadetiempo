package dom.modules;

import dom.modules.hoja.AjustadorFecha;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseService  extends AbstractFactoryAndRepository {
	protected final static AjustadorFecha ajustadorFecha = new AjustadorFecha();
	protected final static Logger LOG = LoggerFactory.getLogger(BaseService.class);
}
