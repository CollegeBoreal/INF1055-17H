import { MonappPage } from './app.po';

describe('monapp App', function() {
  let page: MonappPage;

  beforeEach(() => {
    page = new MonappPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
