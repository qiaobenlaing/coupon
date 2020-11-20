//
//  BMSQ_HomeActivityView.m
//  BMSQC
//
//  Created by gh on 15/11/21.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_HomeActivityView.h"
#import "HomeButton.h"

@implementation BMSQ_HomeActivityView

- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setViewUp];

    }
    return self;
    
}


- (void)setViewUp {
    
}

- (void)setHomeActivityView:(NSDictionary *)dic {
    
    int template = [[dic objectForKey:@"template"] intValue];
    NSArray *array = [dic objectForKey:@"subList"];
    

    if (template == 201) {
        for (int i=0; i<array.count; i++) {
            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
            
            UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)
                                                         object:subDic
                                                            tag:tag];
            [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
            
            
            if (i==0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i==1) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }
            
            [self addSubview:btn];
            
        }
        
    }else if (template == 202) {
        for (int i=0; i<array.count; i++) {
            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            
            UIButtonEx *button = [HomeButton createParallelActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT)
                                                                object:subDic                                                                   tag:tag];
            [button addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            if (i==0) {
                button.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
                
            }else if (i==1) {
                button.frame = CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT);
                
            }
            [self addSubview:button];

        }
    }else if (template == 301) {
        for (int i=0; i<array.count; i++) {
            NSInteger tag = 50000+i+1;
            NSDictionary *subDic = array[i];
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            if (i==0) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];
                
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createParallelActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];

                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }
            
        }
    }else if (template == 302) {
        for (int i=0; i<array.count; i++) {
            NSInteger tag = 50000+i+1;
            NSDictionary *subDic = array[i];
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            if (i==0) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*2) object:subDic tag:tag];
                [self addSubview:btn];
                
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];
                
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];
                
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }
            
        }
        
    }else if (template == 303) {
        for (int i=0; i<array.count; i++) {
            NSInteger tag = 50000+i+1;
            NSDictionary *subDic = array[i];
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            if (i==0) {
                UIButtonEx *btn = [HomeButton createParallelActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }
        }
        
    }else if (template == 304) {
        for (int i=0; i<array.count; i++) {
            NSInteger tag = 50000+i+1;
            NSDictionary *subDic = array[i];
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
            if (i==0) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*2) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }
            
        }
        
    }else if (template == 401) {
        self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*2);
        
        for (int i=0; i<array.count; i++) {
            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            
            UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)
                                                         object:subDic
                                                            tag:tag];
            [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
            
            
            if (i==0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i==1) {
                btn.frame = CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i ==2) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i ==3) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }
            
            [self addSubview:btn];
            
        }
    }else if (template == 506) {
        for (int i=0; i<array.count; i++) {

            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*3);
            
            if (i==0) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)
                                                             object:subDic
                                                                tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)
                                                             object:subDic
                                                                tag:tag];
                [self addSubview:btn];
                
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];

            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }else if (i ==3) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*3/2) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
                
            }else if (i ==4) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*3/2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*3/2) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
            }
            
        }
    }else if (template == 507) {
        for (int i=0; i<array.count; i++) {
            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            
            self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*3);
            
            if (i==0) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*3/2) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
                
            }else if (i==1) {
                UIButtonEx *btn = [HomeButton createUprightActivityBtn:CGRectMake(0, HOME_MODULE_HEIGHT*3/2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*3/2) object:subDic tag:tag];
                [self addSubview:btn];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                
            }else if (i ==2) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];
                
            }else if (i ==3) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic tag:tag];
                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
                [self addSubview:btn];

                
                
            }else if (i ==4) {
                UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT) object:subDic  tag:tag];
                [self addSubview:btn];

                [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
            }
        }
    }else if (template == 601) {
        
        self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, HOME_MODULE_HEIGHT*3);
        for (int i = 0; i<array.count; i++) {
            NSDictionary *subDic = array[i];
            NSInteger tag = 50000+i+1;
            UIButtonEx *btn = [HomeButton createHomeActivityBtn:CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT)
                                                         object:subDic
                                                            tag:tag];
            [btn addTarget:self action:@selector(btnAct:) forControlEvents:UIControlEventTouchUpInside];
            
            if (i==0) {
                btn.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i==1) {
                btn.frame = CGRectMake(0, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
               
            }else if (i ==2) {
                btn.frame = CGRectMake(0, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);

            }else if (i ==3) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
              
            }else if (i ==4) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);
                
            }else if (i ==5) {
                btn.frame = CGRectMake(APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT*2, APP_VIEW_WIDTH/2, HOME_MODULE_HEIGHT);

            }
            [self addSubview:btn];
            
        }
    }
}


- (void)btnAct:(UIButtonEx *)sender {
    
    if (self.delegate != nil) {
        NSLog(@"代理方法活动-------%ld",(long)sender.tag);
        [self.delegate clickHomeActivity:sender.object];
        
        
    }
    
}



@end
