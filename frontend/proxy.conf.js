/**
 * @type {import("webpack-dev-server").ProxyConfigArrayItem} config
 */
const config = {
  /**
   * Mock response for /api/v1/benutzer/stammdaten/sachbearbeiter
   *
   * @example
   * bypass: (req, res) => {
   *   switch (req.url) {
   *     case '/api/v1/benutzer/stammdaten/sachbearbeiter':
   *       return res.end(
   *         JSON.stringify(require('./mock/buchstaben-zuweisung.json')),
   *       );
   *   }
   * },
   */
  target: 'http://localhost:8080',
  secure: false,
  changeOrigin: true,
};

module.exports = {
  '/api/v1': config,
  watchOptions: {
    ignored: /node_modules/,
  },
};
