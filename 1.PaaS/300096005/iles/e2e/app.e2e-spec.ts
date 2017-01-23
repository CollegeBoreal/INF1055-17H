import { IlesPage } from './app.po';

describe('iles App', function() {
  let page: IlesPage;

  beforeEach(() => {
    page = new IlesPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
