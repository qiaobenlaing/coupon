//
//  UITabBarControllerEX.m
//  51ZhangDan
//
//  Created by cq on 12-12-26.
//  Copyright (c) 2012年 djx. All rights reserved.
//
#import "UITabBarControllerEx.h"
#import "UITabBarEx.h"
#define kTabBarHeight (APP_TABBAR_HEIGHT)

static UITabBarControllerEx *leveyTabBarController;

@implementation UIViewController (LeveyTabBarControllerSupport)

- (UITabBarControllerEx *)leveyTabBarController
{
	return leveyTabBarController;
}

@end

@interface UITabBarControllerEx()
- (void)displayViewAtIndex:(NSUInteger)index;
@end

@implementation UITabBarControllerEx
@synthesize delegate = _delegate;
@synthesize selectedViewController = _selectedViewController;
@synthesize viewControllers = _viewControllers;
@synthesize selectedIndex = _selectedIndex;
@synthesize tabBarHidden = _tabBarHidden;
@synthesize animateDriect;

#pragma mark -
#pragma mark lifecycle
- (id)initWithViewControllers:(NSArray *)vcs imageArray:(NSArray *)arr
{
	self = [super init];
	if (self != nil)
	{
		_viewControllers = [NSMutableArray arrayWithArray:vcs];
		m_arrayTableBarItem = [NSMutableArray arrayWithArray:arr];
		_containerView = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
		
		_transitionView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, _containerView.frame.size.height - kTabBarHeight)];
		_transitionView.backgroundColor =  [UIColor groupTableViewBackgroundColor];
		
		_tabBar = [[UITabBarEx alloc] initWithFrame:CGRectMake(0, _containerView.frame.size.height - kTabBarHeight, APP_VIEW_WIDTH, kTabBarHeight) buttonImages:arr];
		_tabBar.delegate = self;
		
         //CGRect rect = self.view.frame;
        
        leveyTabBarController = self;
        animateDriect = 0;
        m_imageviewLine = [[UIImageView alloc]initWithFrame:CGRectMake(0, _containerView.frame.size.height - 2, 320.0f/[arr count], 2)];
        m_imageviewLine.image = [UIImage imageNamed:@"tableBar_Line"];
        [self.view addSubview:m_imageviewLine];
        
       
	}
	return self;
}

- (void)loadView 
{
	[super loadView];
	
	[_containerView addSubview:_transitionView];
	[_containerView addSubview:_tabBar];
	self.view = _containerView;
}

- (void)viewDidLoad 
{
    [super viewDidLoad];
	
    self.selectedIndex = 0;
}

- (void)viewDidUnload
{
	[super viewDidUnload];
	
	_tabBar = nil;
	_viewControllers = nil;
}

#pragma mark - instant methods

- (UITabBarEx *)tabBar
{
	return _tabBar;
}

- (BOOL)tabBarTransparent
{
	return _tabBarTransparent;
}

- (void)setTabBarTransparent:(BOOL)yesOrNo
{
	if (yesOrNo == YES)
	{
		_transitionView.frame = _containerView.bounds;
	}
	else
	{
		_transitionView.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, _containerView.frame.size.height - kTabBarHeight);
	}
}

- (void)hidesTabBar:(BOOL)yesOrNO animated:(BOOL)animated
{
    [m_imageviewLine setHidden:yesOrNO];
    [self hidesTabBar:yesOrNO animated:animated driect:animateDriect];
}

- (void)hidesTabBar:(BOOL)yesOrNO animated:(BOOL)animated driect:(NSInteger)driect
{
    // driect: 0 -- 上下  1 -- 左右
    
    NSInteger kTabBarWidth = [[UIScreen mainScreen] bounds].size.width;
    
	if (yesOrNO == YES)
	{
        if (driect == 0)
        {
            if (self.tabBar.frame.origin.y == self.view.frame.size.height)
            {
                return;
            }
        }
        else
        {
            if (self.tabBar.frame.origin.x == 0 - kTabBarWidth)
            {
                return;
            }
        }
	}
	else 
	{
        if (driect == 0)
        {
            if (self.tabBar.frame.origin.y == self.view.frame.size.height - kTabBarHeight)
            {
                return;
            }
        }
        else
        {
            if (self.tabBar.frame.origin.x == 0)
            {
                return;
            }
        }
	}
	
	if (animated == YES)
	{
		[UIView beginAnimations:nil context:NULL];
		[UIView setAnimationDuration:0.3f];
		if (yesOrNO == YES)
		{
            if (driect == 0)
            {
                self.tabBar.frame = CGRectMake(self.tabBar.frame.origin.x, self.tabBar.frame.origin.y + kTabBarHeight, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
            else
            {
                self.tabBar.frame = CGRectMake(0 - kTabBarWidth, self.tabBar.frame.origin.y, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
		}
		else 
		{
            if (driect == 0)
            {
                self.tabBar.frame = CGRectMake(self.tabBar.frame.origin.x, self.tabBar.frame.origin.y - kTabBarHeight, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
            else
            {
                self.tabBar.frame = CGRectMake(0, self.tabBar.frame.origin.y, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
		}
		[UIView commitAnimations];
	}
	else 
	{
		if (yesOrNO == YES)
		{
            if (driect == 0)
            {
                self.tabBar.frame = CGRectMake(self.tabBar.frame.origin.x, self.tabBar.frame.origin.y + kTabBarHeight, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
            else
            {
                self.tabBar.frame = CGRectMake(0 - kTabBarWidth, self.tabBar.frame.origin.y, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
		}
		else 
		{
            if (driect == 0)
            {
                self.tabBar.frame = CGRectMake(self.tabBar.frame.origin.x, self.tabBar.frame.origin.y - kTabBarHeight, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
            else
            {
                self.tabBar.frame = CGRectMake(0, self.tabBar.frame.origin.y, self.tabBar.frame.size.width, self.tabBar.frame.size.height);
            }
		}
	}
    
    float timer = 0.3;
    if (!animated)
    {
        timer = 0;
    }
    [UIView animateWithDuration:timer animations:^{
        
        CGRect rect = self.tabBar.frame;
        
        BOOL isHidden = yesOrNO;
        
//        if (rect.origin.y == APP_VIEW_HEIGHT - 49)
//        {
//            rect.origin.y += 49;
//            isHidden = YES;
//        }
//        else
//        {
//            rect.origin.y = APP_VIEW_HEIGHT - 49;
//            isHidden = NO;
//        }
        self.tabBar.frame = rect;
        
        for(UIView *view in self.view.subviews)
        {
			if([view isKindOfClass:[UITabBar class]])
            {   //处理UITabBar视图
                
			}
            else
            {   //处理其它视图
                
				if (isHidden)
                {
					[view setFrame:CGRectMake(view.frame.origin.x, view.frame.origin.y, view.frame.size.width,APP_VIEW_HEIGHT)];
				}
                else
                {
					[view setFrame:CGRectMake(view.frame.origin.x, view.frame.origin.y, view.frame.size.width,APP_VIEW_HEIGHT - kTabBarHeight)];
				}
            }
        }
    } completion:^(BOOL finished) {
        
    }];
}

- (NSUInteger)selectedIndex
{
	return _selectedIndex;
}

- (UIViewController *)selectedViewController
{
    return [_viewControllers objectAtIndex:_selectedIndex];
}

-(void)setSelectedIndex:(NSUInteger)index
{
    [self displayViewAtIndex:index];
    [_tabBar selectTabAtIndex:index];
}

- (void)removeViewControllerAtIndex:(NSUInteger)index
{
    if (index >= [_viewControllers count])
    {
        return;
    }
    // Remove view from superview.
    [[(UIViewController *)[_viewControllers objectAtIndex:index] view] removeFromSuperview];
    // Remove viewcontroller in array.
    [_viewControllers removeObjectAtIndex:index];
    // Remove tab from tabbar.
    [_tabBar removeTabAtIndex:index];
}

- (void)insertViewController:(UIViewController *)vc withImageDic:(NSDictionary *)dict atIndex:(NSUInteger)index
{
    [_viewControllers insertObject:vc atIndex:index];
    [_tabBar insertTabWithImageDic:dict atIndex:index];
}


#pragma mark - Private methods
- (void)displayViewAtIndex:(NSUInteger)index
{
    //设置tablecontroller背景
    // Before change index, ask the delegate should change the index.
    if ([_delegate respondsToSelector:@selector(tabBarController:shouldSelectViewController:)]) 
    {
        if (![_delegate tabBarController:self shouldSelectViewController:[self.viewControllers objectAtIndex:index]])
        {
            return;
        }
    }
    // If target index if equal to current index, do nothing.
    if (_selectedIndex == index && [[_transitionView subviews] count] != 0) 
    {
        return;
    }

    //[self setImageViewLineAnimation:index];
    _selectedIndex = index;
    
	UIViewController *selectedVC = [self.viewControllers objectAtIndex:index];

	selectedVC.view.frame = _transitionView.frame;
	if ([selectedVC.view isDescendantOfView:_transitionView]) 
	{
		[_transitionView bringSubviewToFront:selectedVC.view];
	}
	else
	{
		[_transitionView addSubview:selectedVC.view];
	}
    
    // Notify the delegate, the viewcontroller has been changed.
    if ([_delegate respondsToSelector:@selector(tabBarController:didSelectViewController:)])
    {
        [_delegate tabBarController:self didSelectViewController:selectedVC];
    }
    
}

#pragma mark -
#pragma mark tabBar delegates
- (void)tabBar:(UITabBarEx *)tabBar didSelectIndex:(NSInteger)index
{
	[self displayViewAtIndex:index];
}

- (void)setImageViewLineAnimation:(NSInteger)index
{
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3f];
    CGRect rect = CGRectMake(index*APP_VIEW_WIDTH/[m_arrayTableBarItem count], _containerView.frame.size.height - 2, APP_VIEW_WIDTH/[m_arrayTableBarItem count], 2);
    m_imageviewLine.frame = rect;
    [UIView commitAnimations];
}
@end
