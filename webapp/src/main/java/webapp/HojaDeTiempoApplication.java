package webapp;

import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchTheme;
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider;
import org.apache.isis.viewer.wicket.viewer.IsisWicketApplication;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class HojaDeTiempoApplication extends IsisWicketApplication {

    private static final long serialVersionUID = 1L;

    @Override
    protected void init() {
        super.init();
        IBootstrapSettings settings = Bootstrap.getSettings();
        settings.setThemeProvider(new BootswatchThemeProvider(BootswatchTheme.Simplex));
    }

    @Override
    public Session newSession(final Request request, final Response response) {
        return super.newSession(request, response);
    }

    @Override
    public WebRequest newWebRequest(HttpServletRequest servletRequest, String filterPath) {
        return super.newWebRequest(servletRequest, filterPath);
    }

    @Override
    protected Module newIsisWicketModule() {
        final Module isisDefaults = super.newIsisWicketModule();
        final Module overrides = new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).annotatedWith(Names.named("applicationName")).toInstance("Hoja de Tiempo");
                bind(String.class).annotatedWith(Names.named("applicationCss")).toInstance("css/application.css");
                bind(String.class).annotatedWith(Names.named("applicationJs")).toInstance("scripts/application.js");
                bind(String.class).annotatedWith(Names.named("welcomeMessage")).toInstance(
                        readLines(getClass(), "welcome.html"));
                bind(String.class).annotatedWith(Names.named("aboutMessage")).toInstance("Hoja de Tiempo");
                bind(InputStream.class).annotatedWith(Names.named("metaInfManifest")).toProvider(
                        Providers.of(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF")));
            }
        };
        return Modules.override(isisDefaults).with(overrides);
    }

    private static String readLines(final Class<?> contextClass, final String resourceName) {
        try {
            List<String> readLines = Resources.readLines(Resources.getResource(contextClass, resourceName),
                    Charset.defaultCharset());
            final String aboutText = Joiner.on("\n").join(readLines);
            return aboutText;
        } catch (IOException e) {
            return "Registro de Hoja de Tiempo";
        }
    }

}
