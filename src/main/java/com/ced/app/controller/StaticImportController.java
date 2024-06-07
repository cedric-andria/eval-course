package com.ced.app.controller;

import java.util.HashMap;

public interface StaticImportController {
    public static HashMap<String, Object> UIconfig = new HashMap<>() {{
        put("nbElementsPerPage", 2);
    }};

    public static String settings = "<a class=\"fixed-plugin-button text-dark position-fixed px-3 py-2\">\r\n" + //
                "      <i class=\"material-icons py-2\">settings</i>\r\n" + //
                "    </a>\r\n" + //
                "    <div class=\"card shadow-lg\">\r\n" + //
                "      <div class=\"card-header pb-0 pt-3\">\r\n" + //
                "        <div class=\"float-start\">\r\n" + //
                "          <h5 class=\"mt-3 mb-0\">UI Configurator</h5>\r\n" + //
                "          <p>Change as you please</p>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"float-end mt-4\">\r\n" + //
                "          <button class=\"btn btn-link text-dark p-0 fixed-plugin-close-button\">\r\n" + //
                "            <i class=\"material-icons\">clear</i>\r\n" + //
                "          </button>\r\n" + //
                "        </div>\r\n" + //
                "        <!-- End Toggle Button -->\r\n" + //
                "      </div>\r\n" + //
                "      <hr class=\"horizontal dark my-1\">\r\n" + //
                "      <div class=\"card-body pt-sm-3 pt-0\">\r\n" + //
                "        <!-- Sidebar Backgrounds -->\r\n" + //
                "        <!-- Sidenav Type -->\r\n" + //
                "        <div class=\"mt-3\">\r\n" + //
                "          <h6 class=\"mb-0\">Sidenav Type</h6>\r\n" + //
                "          <p class=\"text-sm\">Choose between 2 different sidenav types.</p>\r\n" + //
                "        </div>\r\n" + //
                "        <div class=\"d-flex\">\r\n" + //
                "          <button class=\"btn bg-gradient-dark px-3 mb-2 active\" data-class=\"bg-gradient-dark\" onclick=\"sidebarType(this)\">Dark</button>\r\n" + //
                "          <button class=\"btn bg-gradient-dark px-3 mb-2 ms-2\" data-class=\"bg-transparent\" onclick=\"sidebarType(this)\">Transparent</button>\r\n" + //
                "          <button class=\"btn bg-gradient-dark px-3 mb-2 ms-2\" data-class=\"bg-white\" onclick=\"sidebarType(this)\">White</button>\r\n" + //
                "        </div>\r\n" + //
                "        <p class=\"text-sm d-xl-none d-block mt-2\">You can change the sidenav type just on desktop view.</p>\r\n" + //
                "        <!-- Navbar Fixed -->\r\n" + //
                "        <hr class=\"horizontal dark my-3\">\r\n" + //
                "        <div class=\"mt-2 d-flex\">\r\n" + //
                "          <h6 class=\"mb-0\">Light / Dark</h6>\r\n" + //
                "          <div class=\"form-check form-switch ps-0 ms-auto my-auto\">\r\n" + //
                "            <input class=\"form-check-input mt-1 ms-auto\" type=\"checkbox\" id=\"dark-version\" onclick=\"darkMode(this)\">\r\n" + //
                "          </div>\r\n" + //
                "        </div>\r\n" + //
                "        <hr class=\"horizontal dark my-sm-4\">\r\n" + //
                "        \r\n" + //
                "        </div>\r\n" + //
                "      </div>\r\n" + //
                "    </div>";

    public static String head_imports = "<meta charset=\"utf-8\" />\r\n" + //
    "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" + //
    "  <link rel=\"apple-touch-icon\" sizes=\"76x76\" href=\"img/construction.png\">\r\n" + //
    "  <link rel=\"icon\" type=\"image/png\" href=\"img/marathon.png\">\r\n" + //
    "  <title>\r\n" + //
    "      Eval course\r\n" + //
    "  </title>\r\n" + //
    "  <!--     Fonts and icons     -->\r\n" + //
    "  <link rel=\"stylesheet\" type=\"text/css\" href=\"https://fonts.googleapis.com/css?family=Roboto:300,400,500,700,900|Roboto+Slab:400,700\" />\r\n" + //
    "  <!-- Nucleo Icons -->\r\n" + //
    "  <link href=\"css/nucleo-icons.css\" rel=\"stylesheet\" />\r\n" + //
    "  <link href=\"css/nucleo-svg.css\" rel=\"stylesheet\" />\r\n" + //
    "  <!-- Font Awesome Icons -->\r\n" + //
    "  <script src=\"https://kit.fontawesome.com/42d5adcbca.js\" crossorigin=\"anonymous\"></script>\r\n" + //
    "  <!-- Material Icons -->\r\n" + //
    "  <link href=\"https://fonts.googleapis.com/icon?family=Material+Icons+Round\" rel=\"stylesheet\">\r\n" + //
    "  <!-- CSS Files -->\r\n" + //
    "  <link id=\"pagestyle\" href=\"css/material-dashboard.css?v=3.1.0\" rel=\"stylesheet\" />\r\n" + //
    "  <!-- Nepcha Analytics (nepcha.com) -->\r\n" + //
    "  <!-- Nepcha is a easy-to-use web analytics. No cookies and fully compliant with GDPR, CCPA and PECR. -->\r\n" + //
    "  <script defer data-site=\"YOUR_DOMAIN_HERE\" src=\"https://api.nepcha.com/js/nepcha-analytics.js\"></script>";

    public static String foot_imports = "<script src=\"js/core/popper.min.js\"></script>\r\n" + //
        "  <script src=\"js/core/bootstrap.min.js\"></script>\r\n" + //
        "  <script src=\"js/plugins/perfect-scrollbar.min.js\"></script>\r\n" + //
        "  <script src=\"js/plugins/smooth-scrollbar.min.js\"></script>\r\n" + //
        "  <script src=\"js/plugins/chartjs.min.js\"></script\r\n>" + 
        "  <script src=\"js/material-dashboard.min.js?v=3.1.0\"></script>";

    public static String footer = "<footer class=\"footer position-absolute bottom-2 py-2 w-100\">\r\n" + //
    "        <div class=\"container\">\r\n" + //
    "          <div class=\"row align-items-center justify-content-lg-between\">\r\n" + //
    "            <div class=\"col-12 col-md-6 my-auto\">\r\n" + //
    "              <div class=\"copyright text-center text-sm text-white text-lg-start\">\r\n" + //
    "                © ANDRIAMBELO Tantely Cedric\r\n" + //
    "              </div>\r\n" + //
    "            </div>\r\n" + //
    "            <div class=\"col-12 col-md-6\">\r\n" + //
    "              <ul class=\"nav nav-footer justify-content-center justify-content-lg-end\">\r\n" + //
    "                <li class=\"nav-item\">\r\n" + //
    "                  <script>\r\n" + //
    "                    document.write(new Date().getFullYear())\r\n" + //
    "                  </script>\r\n" + //
    "                </li>\r\n" + //
    "                <li class=\"nav-item\">\r\n" + //
    "                  <a href=\"#\" class=\"nav-link text-white\">ETU1381</a>\r\n" + //
    "                </li>\r\n" + //
    "                <li class=\"nav-item\">\r\n" + //
    "                  <a href=\"#\" class=\"nav-link text-white\"></a>\r\n" + //
    "                </li>\r\n" + //
    "                <li class=\"nav-item\">\r\n" + //
    "                  <a href=\"#\" class=\"nav-link text-white\"></a>\r\n" + //
    "                </li>\r\n" + //
    "                \r\n" + //
    "              </ul>\r\n" + //
    "            </div>\r\n" + //
    "          </div>\r\n" + //
    "        </div>\r\n" + //
    "      </footer>";
    
    public static String sidebar = "<aside class=\"sidenav navbar navbar-vertical navbar-expand-xs border-0 border-radius-xl my-3 fixed-start ms-3   bg-gradient-dark\" id=\"sidenav-main\">\r\n" + //
            "    <div class=\"sidenav-header\">\r\n" + //
            "      <i class=\"fas fa-times p-3 cursor-pointer text-white opacity-5 position-absolute end-0 top-0 d-none d-xl-none\" aria-hidden=\"true\" id=\"iconSidenav\"></i>\r\n" + //
            "      <a class=\"navbar-brand m-0\" href=\"#\" target=\"_blank\">\r\n" + //
            "        <img src=\"img/marathon.png\" class=\"navbar-brand-img h-100\" alt=\"main_logo\">\r\n" + //
            "        <span class=\"ms-4 font-weight-bold text-white\">Eval Course</span>\r\n" + //
            "      </a>\r\n" + //
            "    </div>\r\n" + //
            "    <hr class=\"horizontal light mt-0 mb-2\">\r\n" + //
            "    <div class=\"collapse navbar-collapse  w-auto \" id=\"sidenav-collapse-main\">\r\n" + //
            "      <ul class=\"navbar-nav\">\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white\" href=\"/attemptloginuser?pageNumber=0\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">account_tree</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Liste etapes</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white\" href=\"/getclassement_etape\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">leaderboard</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Classement par etape</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white\" href=\"/getclassement_equipe_categorie\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">groups</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Classement par equipe</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white\" href=\"/gotouploadetapesresultats\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons-outlined opacity-10\">csv</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Import csv Etapes/Resultats</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"/gotouploadpoints\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons-outlined opacity-10\">csv</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Import csv Points</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"/gotogeneratecategorie\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">mediation</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Generation categorie</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"/gotolistepenalites\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">more_time</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Interface penalite admin</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"gotoresetbase\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">restart_alt</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Reinitilisation base</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"gotolisteetape_resultatadmin\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">format_list_numbered</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Liste etape admin Jour 4</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "        <li class=\"nav-item\">\r\n" + //
            "          <a class=\"nav-link text-white \" href=\"deconnexion\">\r\n" + //
            "            <div class=\"text-white text-center me-2 d-flex align-items-center justify-content-center\">\r\n" + //
            "              <i class=\"material-icons opacity-10\">logout</i>\r\n" + //
            "            </div>\r\n" + //
            "            <span class=\"nav-link-text ms-1\">Se deconnecter</span>\r\n" + //
            "          </a>\r\n" + //
            "        </li>\r\n" + //
            "      </ul>\r\n" + //
            "    </div>\r\n" + //
            "\r\n" + //
            "  </aside>";

}
