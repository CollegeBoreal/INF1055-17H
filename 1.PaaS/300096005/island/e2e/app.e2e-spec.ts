import { IslandPage } from './app.po';

describe('island App', function() {
  let page: IslandPage;

  beforeEach(() => {
    page = new IslandPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
