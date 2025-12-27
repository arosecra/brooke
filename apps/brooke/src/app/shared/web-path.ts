export class WebPath {
  static parent(path: string) {
    let parts = path.split('/').filter((val) => val !== '');
    parts.splice(parts.length - 1, 1);
    return '/' + parts.join('/');
  }

  static basename(path: string) {
    let parts = path.split('/').filter((val) => val !== '');
    parts.splice(parts.length - 1, 1);
    return '/' + parts.join('/');
  }
}
