    //
//  actionPerform.m
//  IcardEnglish
//
//  Created by djx on 12-8-30.
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "actionPerform.h"

#define PAGESCROLLVIEW_HEIGHT APP_VIEW_HEIGHT

@implementation actionPerform

// The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
/*
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization.
    }
    return self;
}
*/

/*
// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
}
*/

/*
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
}
*/

/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (id)init
{
    self = [super init];
    if (self)
    {
        [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(closeSelf) name:@"closeActionView" object:nil];
    }
    return self;
}

- (void)viewDidLoad
{
	[super viewDidLoad];

	NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
	NSString* isFirstLogin = [SaveDefaults objectForKey:@"isFirstLogin"];
	
	pages = [[NSMutableArray alloc]init];
    int count = 3;

	for (int i = 1; i <= count; i++)
	{
		int offset = 0;
		if (![isFirstLogin isEqualToString:@"0"])
		{
			offset = 40;
		}
		UIView* v = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, PAGESCROLLVIEW_HEIGHT)];
		UIImageView* imageView = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, PAGESCROLLVIEW_HEIGHT)];
        
//		NSString* name = [NSString stringWithFormat:@"action_pic_%d",i];
//        if (iPhone5)
//        {
            NSString* name = [NSString stringWithFormat:@"action_pic_ip5_%d",i];
       // }
        
		imageView.image = [UIImage imageNamed:name];
		
		[v addSubview:imageView];


		if (i == count)
		{
            UIButton* btnClose = [[UIButton alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-120)/2, APP_VIEW_HEIGHT - 138, 120, 50)];
            //[btnClose setTitle:@"点击开启" forState:UIControlStateNormal];
            //[btnClose setBackgroundImage:[UIImage imageNamed:@"what_start"] forState:UIControlStateNormal];
            [btnClose addTarget:self action:@selector(closeSelf) forControlEvents:UIControlEventTouchUpInside];
            
            [v addSubview:btnClose];
		}
		
		[pages addObject:v];
		
	}
	
	if ([isFirstLogin isEqualToString:@"1"])
	{
		scrollView = [[PageScrollView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, PAGESCROLLVIEW_HEIGHT) pageRect:CGRectMake(0,0,APP_VIEW_WIDTH,PAGESCROLLVIEW_HEIGHT) controlRect:CGRectMake((APP_VIEW_WIDTH - 100)/2,PAGESCROLLVIEW_HEIGHT - 110,100,10)];
	}
	else
	{
		scrollView = [[PageScrollView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, PAGESCROLLVIEW_HEIGHT) pageRect:CGRectMake(0,0,APP_VIEW_WIDTH,PAGESCROLLVIEW_HEIGHT-40) controlRect:CGRectMake((APP_VIEW_WIDTH - 100)/2,PAGESCROLLVIEW_HEIGHT-35,100,10)];
	}	
	
	scrollView.pages = pages;
	scrollView.delegate = self;
	
	[self.view addSubview:scrollView];
    
   pageControl = [[UIPageControl alloc]initWithFrame:CGRectMake((APP_VIEW_WIDTH-80)/2, APP_VIEW_HEIGHT - 50, 80, 30)];
    pageControl.numberOfPages = pages.count;
    [self.view addSubview:pageControl];
}

- (void)closeSelf
{
    NSUserDefaults *SaveDefaults = [NSUserDefaults standardUserDefaults];
    [SaveDefaults setObject:@"0" forKey:@"isFirstLogin"];
//    [self dismissViewControllerAnimated:YES completion:nil];
    
    if (self.loadingViewControllerFinish) {
        self.loadingViewControllerFinish();
    }
}

- (void)viewDidAppear:(BOOL)animated
{
	
}

-(void)pageScrollViewDidChangeCurrentPage:(PageScrollView *)pagescrollView currentPage:(int)currentPage
{
    pageControl.currentPage = currentPage;
}


@end
